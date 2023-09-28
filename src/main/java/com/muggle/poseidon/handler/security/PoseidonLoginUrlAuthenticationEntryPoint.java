package com.muggle.poseidon.handler.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @program: poseidon
 * @description: 未登录处理
 * @author: muggle
 * @create: 2018-12-31
 **/
public class PoseidonLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private static final Log log = LogFactory.getLog(PoseidonLoginUrlAuthenticationEntryPoint.class);

    public PoseidonLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error(">>>>>>>>>>>>>>>>>>>>> 用户未登陆 <<<<<<<<<<<<<<<<<<<<<<<<<");
        response.setContentType("application/json;charset=UTF-8");
        final PrintWriter writer = response.getWriter();
        writer.write("{\"code\":401,\"message\":\"用户未登录\"}");
        writer.close();
    }
}
