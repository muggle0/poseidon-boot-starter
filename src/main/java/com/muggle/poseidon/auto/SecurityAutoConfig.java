package com.muggle.poseidon.auto;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.muggle.poseidon.adapter.PoseidonAuthConfigAdapter;
import com.muggle.poseidon.aop.RequestAspect;
import com.muggle.poseidon.base.DistributedLocker;
import com.muggle.poseidon.base.exception.SimplePoseidonException;
import com.muggle.poseidon.entity.AuthUrlPathDO;
import com.muggle.poseidon.handler.security.RequestLogProcessor;
import com.muggle.poseidon.service.TokenService;
import com.muggle.poseidon.store.SecurityStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @program: poseidon-cloud-starter
 * @description: 自动化配置核心类
 * @author: muggle
 * @create: 2019-11-04
 **/

@Configuration
@EnableConfigurationProperties(PoseidonSecurityProperties.class)
@ConditionalOnProperty(prefix = "poseidon", name = "auto", havingValue = "true", matchIfMissing = false)
//@ConditionalOnProperty(TokenService.class)
@SuppressWarnings("ALL")
public class SecurityAutoConfig {
    private static final Log log = LogFactory.getLog(SecurityAutoConfig.class);

    @Autowired
    PoseidonSecurityProperties properties;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired(required = false)
    RequestLogProcessor logProcessor;

    @Bean
    public PoseidonAuthConfigAdapter getAdapter(TokenService tokenService, SecurityStore securityStore) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>> 开启自动化配置 <<<<<<<<<<<<<<<<<<<<<");
        if (securityStore == null) {
            throw new SimplePoseidonException("请先注册 securityStore");
        }
        return new PoseidonAuthConfigAdapter(tokenService, securityStore, properties);
    }

    /**
     * 测试和生产环境生效
     *
     * @param tokenService
     * @return
     */
    @Bean
    @Profile({"uat", "sit", "online", "refresh"})
    public CommandLineRunner init(final TokenService tokenService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                log.debug(">>>>>>>>>>>>>>>>>>>>>>> 权限系统开机任务执行 <<<<<<<<<<<<<<<<<<<<<");
                List<AuthUrlPathDO> allURL = getAllURL();
                tokenService.processUrl(allURL);
            }
        };
    }

    /**
     * 读取所有的url 并交给tokenService.saveUrlInfo处理
     **/
    public List<AuthUrlPathDO> getAllURL() {
        List<AuthUrlPathDO> resultList = new ArrayList<>();

        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodEntry : map.entrySet()) {

            RequestMappingInfo requestMappingInfo = mappingInfoHandlerMethodEntry.getKey();
            HandlerMethod handlerMethod = mappingInfoHandlerMethodEntry.getValue();
            AuthUrlPathDO authUrlPathDO = new AuthUrlPathDO();
            // 类名
            authUrlPathDO.setClassName(handlerMethod.getMethod().getDeclaringClass().getName());

            Annotation[] parentAnnotations = handlerMethod.getBeanType().getAnnotations();
            for (Annotation annotation : parentAnnotations) {
                if (annotation instanceof Api) {
                    Api api = (Api) annotation;
                    authUrlPathDO.setClassDesc(api.value());
                } else if (annotation instanceof RequestMapping) {
                    RequestMapping requestMapping = (RequestMapping) annotation;
                    if (null != requestMapping.value() && requestMapping.value().length > 0) {
                        //类URL
                        authUrlPathDO.setClassUrl(requestMapping.value()[0]);
                    }
                }
            }
            // 方法名
            authUrlPathDO.setMethodName(handlerMethod.getMethod().getName());
            Annotation[] annotations = handlerMethod.getMethod().getDeclaredAnnotations();
            if (annotations != null) {
                // 处理具体的方法信息
                for (Annotation annotation : annotations) {
                    if (annotation instanceof ApiOperation) {
                        ApiOperation methodDesc = (ApiOperation) annotation;
                        String desc = methodDesc.value();
                        //接口描述
                        authUrlPathDO.setMethodDesc(desc);
                    }
                }
            }
            PatternsRequestCondition p = requestMappingInfo.getPatternsCondition();
            for (String url : p.getPatterns()) {
                //请求URL
                authUrlPathDO.setMethodURL(url);
            }
            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                //请求方式：POST/PUT/GET/DELETE
                authUrlPathDO.setRequestType(requestMethod.toString());
            }
            resultList.add(authUrlPathDO);
        }
        return resultList;
    }

    @Bean
    @Autowired
    @ConditionalOnBean(DistributedLocker.class)
    public RequestAspect getLogAspect(DistributedLocker distributedLocker) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>> 日志切面注册 <<<<<<<<<<<<<<<<<<<<<");
        return new RequestAspect(distributedLocker, logProcessor);
    }


    //    配置错误页面
    @Bean
    public WebServerFactoryCustomizer containerCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {
            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {
                log.debug(">>>>>>>>>>>>>>>>>>>>>>> 错误页面配置——404（/public/notfound） 500（/error_message） <<<<<<<<<<<<<<<<<<<<<");
                factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/public/notfound"), new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error_message"));
            }
        };
    }
}
