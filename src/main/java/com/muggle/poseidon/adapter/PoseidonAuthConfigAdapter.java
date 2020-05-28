package com.muggle.poseidon.adapter;

import com.muggle.poseidon.auto.PoseidonSecurityProperties;
import com.muggle.poseidon.filter.SecurityLoginFilter;
import com.muggle.poseidon.filter.SecurityTokenFilter;
import com.muggle.poseidon.handler.*;
import com.muggle.poseidon.manager.PoseidonWebExpressionVoter;
import com.muggle.poseidon.properties.SecurityMessageProperties;
import com.muggle.poseidon.service.TokenService;
import com.muggle.poseidon.store.SecurityStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: poseidon-cloud-starter
 * @description: 权限校验配置类
 * @author: muggle
 * @create: 2019-11-04
 **/

public class PoseidonAuthConfigAdapter extends WebSecurityConfigurerAdapter {
    private static final Log log = LogFactory.getLog(PoseidonAuthConfigAdapter.class);
    private TokenService tokenService;
    private SecurityStore securityStore;
    private PoseidonSecurityProperties properties;

    public PoseidonAuthConfigAdapter(TokenService tokenService, SecurityStore securityStore,PoseidonSecurityProperties properties) {
        this.tokenService = tokenService;
        this.securityStore = securityStore;
        this.properties=properties;
    }

    public PoseidonAuthConfigAdapter(boolean disableDefaults, TokenService tokenService, SecurityStore securityStore) {
        super(disableDefaults);
        this.tokenService = tokenService;
        this.securityStore = securityStore;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        String [] paths={"/**/*.bmp", "/**/*.gif", "/**/*.png", "/**/*.jpg", "/**/*.ico","/**/*.html","/**/*.css"};
        SecurityStore.ACCESS_PATHS.addAll(Arrays.asList(paths));
        web.ignoring().antMatchers(paths);
        SecurityStore.ACCESS_PATHS.add("/error_message");
        SecurityStore.ACCESS_PATHS.add("/");
        SecurityStore.ACCESS_PATHS.add("/public/notfound");
        log.debug("》》》》 初始化security 放行静态资源：{}" + "/**/*.bmp /**/*.png /**/*.gif /**/*.jpg /**/*.ico /**/*.js");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("》》》》 启动security配置");

        List<String> ignorePath = properties.getIgnorePath();
        if (ignorePath==null){
            ignorePath=new ArrayList<>();
        }
        String [] paths=new String[ignorePath.size()];
        ignorePath.toArray(paths);
        SecurityStore.saveAccessPath(ignorePath);
        http.authorizeRequests().antMatchers(paths).permitAll()
                .antMatchers("/admin/oauth/**").hasRole("admin")
                .anyRequest().authenticated().accessDecisionManager(accessDecisionManager())
                .and().csrf().disable();
        http.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/public/login.html").loginProcessingUrl(SecurityMessageProperties.LOGIN_URL).permitAll();
        http.addFilterBefore(poseidonTokenFilter(), LogoutFilter.class);
        http.exceptionHandling().authenticationEntryPoint(loginUrlAuthenticationEntryPoint()).accessDeniedHandler(new PoseidonAccessDeniedHandler());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.addFilterAt(getLoginFilter(),UsernamePasswordAuthenticationFilter.class);
        http.logout().logoutUrl(SecurityMessageProperties.LOGOUT).logoutSuccessHandler(getLogoutSuccessHandler()).permitAll();
    }


    /**
    * @author muggle
    * @Description: 设置投票器
    * @Param:
    * @return:
    * @date 2019/11/6 8:38
    */
    private AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new PoseidonWebExpressionVoter(tokenService));
        return new UnanimousBased(decisionVoters);

    }

    /**
    * @author muggle
    * @Description: token的过滤器
    * @Param:
    * @return:
    * @date 2019/11/6 8:38
    */
    private SecurityTokenFilter poseidonTokenFilter(){
        final SecurityTokenFilter poseidonTokenFilter = new SecurityTokenFilter(securityStore,properties);
        return poseidonTokenFilter;
    }

    /**
    * @author muggle
    * @Description: 未登陆处理器，当url为登陆权限时放行
    * @Param:
    * @return:
    * @date 2019/11/5 12:01
    */
    private AuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        return new PoseidonLoginUrlAuthenticationEntryPoint(SecurityMessageProperties.LOGIN_URL);
    }

    private SecurityLoginFilter getLoginFilter(){
        SecurityLoginFilter securityLoginFilter = new SecurityLoginFilter(tokenService, securityStore);
        securityLoginFilter.setAuthenticationFailureHandler(new PoseidonAuthenticationFailureHandler());
        securityLoginFilter.setAuthenticationSuccessHandler(new PoseidonAuthenticationSuccessHandler());
        securityLoginFilter.setFilterProcessesUrl(SecurityMessageProperties.LOGIN_URL);
        return securityLoginFilter;
    }

    /**
     * 登出成功处理器
     * @return
     */
    private LogoutSuccessHandler getLogoutSuccessHandler(){
        return new PoseidonLogoutSuccessHandler(this.securityStore);
    }
}
