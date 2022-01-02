package com.muggle.poseidon.auto;


import com.muggle.poseidon.aop.RequestAspect;
import com.muggle.poseidon.base.DistributedLocker;
import com.muggle.poseidon.handler.security.RequestLogProcessor;
import com.muggle.poseidon.handler.web.WebUrlHandler;
import com.muggle.poseidon.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 二次开发的扩展配置类
 * muggle
 */

@ConditionalOnProperty(prefix = "poseidon", name = "auto", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(TokenService.class)
@Configuration
public class ExpansibilityConfig {

    @Autowired(required = false)
    private RequestLogProcessor logProcessor;

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ExpansibilityConfig.class);

    @Bean
    @Autowired
    @ConditionalOnBean(DistributedLocker.class)
    public RequestAspect getLogAspect(DistributedLocker distributedLocker) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>> 日志切面注册 <<<<<<<<<<<<<<<<<<<<<");
        return new RequestAspect(distributedLocker, logProcessor);
    }


    //    配置错误页面
    @Bean
    @ConditionalOnBean(WebUrlHandler.class)
    public WebServerFactoryCustomizer containerCustomizer() {
        return (WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>) factory -> {
            log.debug(">>>>>>>>>>>>>>>>>>>>>>> 错误页面配置——404（/public/notfound） 500（/error_message） <<<<<<<<<<<<<<<<<<<<<");
            factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/public/notfound"), new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error_message"));
        };
    }

}
