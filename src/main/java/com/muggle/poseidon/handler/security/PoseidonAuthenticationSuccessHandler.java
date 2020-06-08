package com.muggle.poseidon.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muggle.poseidon.base.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PoseidonAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(PoseidonAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        log.debug("登录成功！");
        response.setContentType("application/json;charset=UTF-8");
        final PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(ResultBean.successData(principal));
        writer.write(result);
        writer.close();
    }
}
