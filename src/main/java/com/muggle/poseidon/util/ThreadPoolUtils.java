package com.muggle.poseidon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/7/28
 **/
public class ThreadPoolUtils {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtils.class);
    private static final String DEFAULT_EXEUTOR_NAME="POSEIDON-DEFAULT-POOL";
    private static final int DEFAULT_POOL_SIEZ=100;
    private static final int DEFAULT_MAX_POOL_SIZE=200;
    private static final int DEFAULT_QUEUE_SIZE=1000;
    private static final long DEFAULT_KEEP_ALIVE=5L;

    private ThreadPoolUtils(){

    }

    public static ThreadPoolExecutor buildDefaultPool(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(DEFAULT_POOL_SIEZ, DEFAULT_MAX_POOL_SIZE, DEFAULT_KEEP_ALIVE, TimeUnit.MINUTES,
                new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE), r -> null);
        return executor;
    }
}
