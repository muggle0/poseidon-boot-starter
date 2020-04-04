package com.muggle.poseidon.base;

import com.muggle.poseidon.base.exception.BasePoseidonCheckException;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-12-31
 **/

public interface PoseidonLocker {

    /**
     * \锁 非阻塞 单位：毫秒
     *
     * @param key
     * @param express
     * @return
     */
    boolean trylock(String key, Long express);

    /**
     * 上锁，阻塞或自旋
     * @param key
     * @param express
     * @throws BasePoseidonCheckException
     */
    void dolock(String key,String value, Long express) throws BasePoseidonCheckException;

    /**
     * 解锁
     * @return
     */
    boolean unlock(String key,String value);

    void clean(String key);

}
