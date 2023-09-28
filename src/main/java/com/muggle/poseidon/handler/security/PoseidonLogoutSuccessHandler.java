package com.muggle.poseidon.handler.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.muggle.poseidon.store.SecurityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @program: poseidon
 * @description: 登出成功处理器
 * @author: muggle
 * @create: 2018-12-31
 **/
public class PoseidonLogoutSuccessHandler implements LogoutSuccessHandler {


    private SecurityStore securityStore;

    public PoseidonLogoutSuccessHandler(SecurityStore securityStore) {
        this.securityStore = securityStore;
    }

    private final static Logger log = LoggerFactory.getLogger("PoseidonLogoutSuccessHandler");

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        if (authentication == null) {
            writer.write("{\"code\":401,\"message\":\"用户未登录\"}");
            writer.close();
            return;
        }
        Object principal = authentication.getPrincipal();
        try {
            Boolean success = securityStore.cleanToken((String) principal);
            if (success) {
                log.info("用户登出, token: {}", principal);
                writer.write("{\"code\":200,\"message\":\"登出成功\"}");
            } else {
                log.info("用户登出失败 token：{}", principal);
                writer.write("{\"code\":5001,\"message\":\"登出失败，请重试\"}");
            }
        } catch (Exception e) {
            log.error("登出失败： ", e);
            writer.write("{\"code\":\"5001\",\"message\":\"" + e.getMessage() + "\"}");
        } finally {
            writer.close();
        }
    }
}
