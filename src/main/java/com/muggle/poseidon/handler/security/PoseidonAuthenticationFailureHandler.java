package com.muggle.poseidon.handler.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录验证失败处理器
 */
public class PoseidonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger log= LoggerFactory.getLogger(PoseidonAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        final PrintWriter writer = httpServletResponse.getWriter();
        writer.write("{\"code\":5001,\"message\":\""+e.getMessage()+"\"}");
        writer.close();
    }
}
