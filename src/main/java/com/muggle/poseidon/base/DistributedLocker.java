package com.muggle.poseidon.base;

import java.util.concurrent.locks.Lock;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-12-31
 **/

public interface DistributedLocker extends Lock {

    /**
     * 锁
     * @param key
     * @param expertime
     * @return
     */
    boolean tryLock(String key, long expertime) throws InterruptedException;

    /**
     * 阻塞
     */
    void lock(String key);

    /**
     * 解锁
     */
    void unlock(String key);
}
