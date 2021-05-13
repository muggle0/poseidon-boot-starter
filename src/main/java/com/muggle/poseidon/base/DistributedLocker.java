package com.muggle.poseidon.base;

import java.util.Map;
import java.util.concurrent.TimeUnit;
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

    /**
     * 比较锁是否一致
     */
    boolean compareLock(Map<String,String> locker);

    /**
     *
     * @param key 锁key
     * @param value 锁 vaule
     * @param time 时长
     * @param unit 单位
     */
    boolean tryLock(String key, String value, int time, TimeUnit unit);

    /**
     * 解锁
     * @param key
     * @param vaule
     * @return
     */
    boolean unlock(String key,String vaule);
}
