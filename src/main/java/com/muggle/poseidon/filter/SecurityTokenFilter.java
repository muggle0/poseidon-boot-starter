package com.muggle.poseidon.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.muggle.poseidon.auto.PoseidonSecurityProperties;
import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.properties.SecurityMessageProperties;
import com.muggle.poseidon.store.SecurityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @program: poseidon-cloud-starter
 * @description: token认证填充用户信息
 * @author: muggle
 * @create: 2019-11-04
 **/

public class SecurityTokenFilter extends OncePerRequestFilter {

    private SecurityStore securityStore;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private PoseidonSecurityProperties properties;


    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(SecurityTokenFilter.class);

    public SecurityTokenFilter(SecurityStore securityStore, PoseidonSecurityProperties properties) {
        this.properties = properties;
        this.securityStore = securityStore;
    }

    /**
     * 该过滤器会首先从请求头中获取token，如果获取失败则会从cookie 中获取token，key都是 token
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @throws AccessDeniedException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, AccessDeniedException {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>> 开始校验token <<<<<<<<<<<<<<<<<<<<<");
        String token = httpServletRequest.getHeader("token");
        Cookie[] cookies = httpServletRequest.getCookies();
        String cookieToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    cookieToken = cookie.getValue();
                }
            }
        }
        if (token == null && cookieToken == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UserDetails userDetails = null;
        try {
            userDetails = securityStore.getUserdetail(token == null ? cookieToken : token);
        } catch (BasePoseidonCheckException e) {
            log.error("》》》》 用户凭证为badToken {}", e.getMessage());
            SecurityContextHolder.getContext().setAuthentication(getBadToken(e.getMessage()));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } catch (Exception e) {
            log.error("》》》》 token 解析异常：", e);
            SecurityContextHolder.getContext().setAuthentication(getBadToken("请求信息非法，无法解析Token"));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (userDetails == null) {
            log.error("该用户不存在， token:{}", token);
            SecurityContextHolder.getContext().setAuthentication(getBadToken("该用户不存在，请重新登录"));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.debug("》》》》 填充token");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private UsernamePasswordAuthenticationToken getBadToken(String message) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(SecurityMessageProperties.BAD_TOKEN, null, null);
        authenticationToken.setDetails(message);
        return authenticationToken;
    }

}
