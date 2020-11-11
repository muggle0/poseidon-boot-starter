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
    boolean tryLock(String key, long expertime);

    /**
     * 阻塞
     */
    boolean tryLock(String key);
}
