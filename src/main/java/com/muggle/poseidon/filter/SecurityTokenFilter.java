package com.muggle.poseidon.filter;


import com.muggle.poseidon.auto.PoseidonSecurityProperties;
import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.entity.SimpleUserDO;
import com.muggle.poseidon.properties.SecurityMessageProperties;
import com.muggle.poseidon.store.SecurityStore;
import com.muggle.poseidon.util.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @program: poseidon-cloud-starter
 * @description: token认证填充用户信息
 * @author: muggle
 * @create: 2019-11-04
 **/

public class SecurityTokenFilter extends OncePerRequestFilter {

    private SecurityStore securityStore;


    /** logger */
    private static final Logger log = LoggerFactory.getLogger(SecurityTokenFilter.class);

    public SecurityTokenFilter(SecurityStore securityStore,PoseidonSecurityProperties properties) {
        this.securityStore = securityStore;
    }

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @throws AccessDeniedException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException ,AccessDeniedException{
        log.debug("》》》》 开始校验token");
        // 如果是开放权限的url直接通过
        String token = httpServletRequest.getHeader("token");
        if (token==null){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        UserDetails userDetails =null;
        try {
            userDetails = securityStore.getUserdetail(token);
        }catch (BasePoseidonCheckException e){
            log.error("》》》》 用户凭证为badToken",e);
            SecurityContextHolder.getContext().setAuthentication(getBadToken(e.getMessage()));
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        if (userDetails==null){
            log.info("该用户不存在 token:{}",token );
            SecurityContextHolder.getContext().setAuthentication(getBadToken("该用户不存在，请重新登录"));
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.debug("》》》》 填充token");
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    private UsernamePasswordAuthenticationToken getBadToken(String message){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(SecurityMessageProperties.BAD_TOKEN,null,null);
        authenticationToken.setDetails(message);
        return authenticationToken;
    }
}
