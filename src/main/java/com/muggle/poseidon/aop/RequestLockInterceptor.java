package com.muggle.poseidon.aop;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @program: hiram_erp
 * @description: 黑名单拦截器
 * @author: muggle
 * @create: 2018-12-06 11:02
 **/
@Deprecated
public  class RequestLockInterceptor implements HandlerInterceptor {

    private List<RequestLockHelper> helpers;

    public RequestLockInterceptor(List<RequestLockHelper> helpers){
        this.helpers=helpers;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag=true;
        if (helpers==null||helpers.isEmpty()){
            return flag;
        }
        for (RequestLockHelper helper : helpers) {
            flag=helper.accsess(request);
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
