package com.muggle.poseidon.handler.security;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestLogProcessor {
    /**
     * 日志记录，进入controller之前 只可读，所以用final 修饰
     *
     * @param request
     * @param args
     */
    void recordBefore(final HttpServletRequest request, final Object[] args);

    /**
     * 日志记录，进入controller 获取返回值 result 为返回值，只可读。
     *
     * @param result 返回值
     * @param args   入参
     */
    void recordAfterReturning(final Object result, final Object[] args);
}
