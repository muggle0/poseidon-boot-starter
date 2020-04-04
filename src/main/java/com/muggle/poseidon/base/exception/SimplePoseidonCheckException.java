package com.muggle.poseidon.base.exception;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-11-06
 **/

public class SimplePoseidonCheckException extends BasePoseidonCheckException {
    private Integer code;

    public SimplePoseidonCheckException(String message) {
        super(message);
        this.code=500;
    }

    public SimplePoseidonCheckException(String message,Integer code) {
        super(message);
        this.code=code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
