//package com.muggle.poseidon.filter;
//
//import com.muggle.poseidon.store.SecurityStore;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import javax.servlet.*;
//import java.io.IOException;
//
//public class DevUserInfoFilter implements Filter {
//    private SecurityStore securityStore;
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        securityStore.getUserdetail()
//        SecurityContextHolder.getContext().setAuthentication();
//        filterChain.doFilter(servletRequest,servletResponse);
//    }
//}
