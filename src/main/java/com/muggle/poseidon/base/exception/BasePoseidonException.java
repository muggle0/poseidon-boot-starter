package com.muggle.poseidon.base.exception;

/**
 * @program: poseidon-cloud-starter
 * @description: 业务异常
 * @author: muggle
 * @create: 2019-11-05
 **/

public abstract class BasePoseidonException extends RuntimeException {

    public BasePoseidonException(String message){
        super(message);
    }

    public abstract Integer getCode();

}
