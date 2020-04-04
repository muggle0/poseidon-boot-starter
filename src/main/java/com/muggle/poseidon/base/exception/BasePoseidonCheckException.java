package com.muggle.poseidon.base.exception;


/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-11-06
 **/

public abstract class BasePoseidonCheckException extends Exception {

    public BasePoseidonCheckException(String message){
        super(message);
    }

    public abstract Integer getCode();

}
