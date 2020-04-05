package com.muggle.poseidon.aop;

import javax.servlet.http.HttpServletRequest;

@Deprecated
public interface RequestLockHelper {

    boolean accsess(HttpServletRequest request);
}
