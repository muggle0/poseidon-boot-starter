package com.muggle.poseidon.filter;

import com.muggle.poseidon.base.exception.BasePoseidonException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-11-05
 **/

@Deprecated
public class PoseidonExceptionTranslationFilter extends ExceptionTranslationFilter {
    public PoseidonExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationEntryPoint);
    }

    public PoseidonExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint, RequestCache requestCache) {
        super(authenticationEntryPoint, requestCache);
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            chain.doFilter(request, response);
            this.logger.debug("Chain processed normally");
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            if (response.isCommitted()) {
                throw new ServletException("Unable to handle the Spring Security Exception because the response is already committed---poseidon-starter PoseidonExceptionTranslationFilter", e);
            }
            if (e instanceof AuthenticationException||e instanceof AccessDeniedException|| e instanceof BasePoseidonException){
                this.logger.debug("请求失败");
                AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException(e.getMessage());
                super.getAuthenticationEntryPoint().commence(request, response, authenticationServiceException);
            }
        }
    }
}
