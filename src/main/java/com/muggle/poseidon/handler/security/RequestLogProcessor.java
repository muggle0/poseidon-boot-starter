package com.muggle.poseidon.handler.security;

import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

public interface RequestLogProcessor {
    /**
     * 日志记录，进入controller之前 只可读，所以用final 修饰
     * @param request
     * @param joinPoint
     */
    void recordBefore(final HttpServletRequest request, final JoinPoint joinPoint);

    /**
     * 日志记录，进入controller 获取返回值 result 为返回值，只可读。
     * @param result
     */
    void recordAfterReturning(final Object result);
}
