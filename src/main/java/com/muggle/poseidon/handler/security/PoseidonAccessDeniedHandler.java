package com.muggle.poseidon.handler.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.muggle.poseidon.properties.SecurityMessageProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @program: poseidon
 * @description: 403处理器 AuthenticationFailureHandler
 * @author: muggle
 * @create: 2018-12-27 19:21
 **/
public class PoseidonAccessDeniedHandler implements AccessDeniedHandler {
    private static final Log log = LogFactory.getLog(PoseidonAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error("用户沒有权限访问 路径： " + request.getRequestURI());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();

        if (SecurityMessageProperties.BAD_TOKEN.equals(authentication.getPrincipal())) {
            response.setContentType("application/json;charset=UTF-8");
            try(PrintWriter writer = response.getWriter();) {
                writer.write("{\"code\":403,\"message\":\"" + details.toString() + "\"}");
                writer.close();
                return;
            }
        }
        if (details instanceof UserDetails) {
            if (!((UserDetails) details).isEnabled()) {
                response.setContentType("application/json;charset=UTF-8");
                try(PrintWriter writer = response.getWriter();) {
                    writer.write("{\"code\":403,\"message\":\"账号过期\"}");
                    return;
                }
            }
            if (!((UserDetails) details).isAccountNonLocked()) {
                response.setContentType("application/json;charset=UTF-8");
                try ( PrintWriter writer = response.getWriter()){
                    writer.write("{\"code\":403,\"message\":\"账号被锁定\"}");
                    return;
                }
            } else {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"code\":403,\"message\":\"没有权限\"}");
                writer.close();
                return;
            }

        } else {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":403,\"message\":\"没有权限\"}");
            writer.close();
            return;
        }

    }
}
