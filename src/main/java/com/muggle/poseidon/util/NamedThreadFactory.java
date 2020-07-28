package com.muggle.poseidon.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/7/28
 **/
public class NamedThreadFactory implements ThreadFactory {
    private final String prefix;
    private final AtomicLong threadIds;

    NamedThreadFactory(String prefix) {
        this.prefix = prefix;
        this.threadIds = new AtomicLong();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.prefix + this.threadIds.incrementAndGet());
        t.setDaemon(false);
        return t;
    }
}
