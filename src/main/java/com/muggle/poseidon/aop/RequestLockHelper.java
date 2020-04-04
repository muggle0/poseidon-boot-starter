package com.muggle.poseidon.aop;

import javax.servlet.http.HttpServletRequest;

public interface RequestLockHelper {

    boolean accsess(HttpServletRequest request);
}
