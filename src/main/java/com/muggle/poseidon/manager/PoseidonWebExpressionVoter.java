package com.muggle.poseidon.manager;

import com.muggle.poseidon.auto.PoseidonSecurityProperties;
import com.muggle.poseidon.properties.SecurityMessageProperties;
import com.muggle.poseidon.service.TokenService;
import com.muggle.poseidon.store.SecurityStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @program: poseidon-cloud-starter
 * @description: 投票器
 * @author: muggle
 * @create: 2019-11-05
 **/

public class PoseidonWebExpressionVoter extends WebExpressionVoter {

    private static final Log log = LogFactory.getLog(PoseidonWebExpressionVoter.class);

    private static AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    private TokenService tokenService;

    private PoseidonSecurityProperties properties;

    public PoseidonWebExpressionVoter(TokenService tokenService,PoseidonSecurityProperties properties) {
        this.properties=properties;
        this.tokenService = tokenService;
    }
    /**
    * @author muggle
    * @Description: 权限校验方法（投票器） 1通过，-1不通过 0弃权
    * @Param: fixme
    * @return:
    * @throws
    * @date 2019/11/5 11:21
    */
    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        String requestUrl = fi.getRequestUrl();
        log.debug("》》》》 请求进入, url：【"+requestUrl+"】用户名：【"+authentication.getPrincipal()+"】");
        /** 获取所有放行权限（在adapter中的config 方法配置）*/
        for (String pattern :
                SecurityStore.ACCESS_PATHS) {
            boolean match = PATH_MATCHER.match(pattern, requestUrl);
            if (match){
                return ACCESS_GRANTED;
            }
        }
        for (String pattern :
                properties.getIgnorePath()) {
            boolean match = PATH_MATCHER.match(pattern, requestUrl);
            if (match){
                return ACCESS_GRANTED;
            }
        }
        boolean bool=tokenVerify(authentication);
        // 未携带token 或者token失效 用户被冻结 用户
        if (bool){
            return ACCESS_DENIED;
        }
        /** 获取用户角色，并通过角色去匹配权限*/
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean b = tokenService.rooleMatch(authorities, requestUrl);
        if (b) {
            return ACCESS_GRANTED;
        }
        log.debug("》》》》 用户无权限访问，用户名：【"+authentication.getPrincipal()+"】");
        return ACCESS_DENIED;
    }

    /**
     * 验证用户是否已登陆，账号是否被锁定或者被冻结
     * @param authentication
     * @return
     */
    private boolean tokenVerify(Authentication authentication) {
        boolean equals = SecurityMessageProperties.BAD_TOKEN.equals(authentication.getPrincipal());
        Object details = authentication.getDetails();
        if (details==null||! (details instanceof UserDetails) ){
            return true;
        }
        UserDetails userInfo=(UserDetails)details;
        boolean accountNonLocked = userInfo.isAccountNonLocked();
        boolean enabled = userInfo.isEnabled();
        return equals || !accountNonLocked || !enabled;
    }
}
