package com.muggle.poseidon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/7/28
 **/
public class ThreadPoolUtils {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtils.class);
    private static final String DEFAULT_EXEUTOR_NAME = "POSEIDON-DEFAULT-POOL-";
    private static final int DEFAULT_POOL_SIEZ = 100;
    private static final int DEFAULT_MAX_POOL_SIZE = 200;
    private static final int DEFAULT_QUEUE_SIZE = 1000;
    private static final long DEFAULT_KEEP_ALIVE = 5L;

    private ThreadPoolUtils() {

    }

    // 1. 线程池工厂 thraedfactory 作用 2. 四种handler
    /**
     * 1. AbortPolicy：丢弃任务并抛出 RejectedExecutionException 异常。（默认这种）
     * 2. DiscardPolicy：也是丢弃任务，但是不抛出异常
     * 3. DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * 4. CallerRunsPolicy：由调用线程处理该任务
     * */
    public static ThreadPoolExecutor buildDefaultPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(DEFAULT_POOL_SIEZ, DEFAULT_MAX_POOL_SIZE, DEFAULT_KEEP_ALIVE, TimeUnit.MINUTES,
                new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE), new NamedThreadFactory(DEFAULT_EXEUTOR_NAME),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    public static ThreadPoolExecutor buildThreadPool(String poolName){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(DEFAULT_POOL_SIEZ, DEFAULT_MAX_POOL_SIZE, DEFAULT_KEEP_ALIVE, TimeUnit.MINUTES,
                new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE), new NamedThreadFactory(poolName),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    public static ThreadPoolExecutor buildThreadPool(String poolName,RejectedExecutionHandler handler){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(DEFAULT_POOL_SIEZ, DEFAULT_MAX_POOL_SIZE, DEFAULT_KEEP_ALIVE, TimeUnit.MINUTES,
                new LinkedBlockingQueue(DEFAULT_QUEUE_SIZE), new NamedThreadFactory(poolName), handler);
        return executor;
    }

   /* public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.buildDefaultPool();
        threadPoolExecutor.execute(()->{
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        });
        threadPoolExecutor.shutdown();
    }*/


}
