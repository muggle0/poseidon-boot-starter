package com.muggle.poseidon.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import com.muggle.poseidon.base.DistributedLocker;
import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;


public class SimpleLocker implements DistributedLocker {


    @Override
    public boolean tryLock(String key, long expertime) throws InterruptedException {
        return false;
    }

    @Override
    public void lock(String key) {

    }

    @Override
    public void unlock(String key) {

    }

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
