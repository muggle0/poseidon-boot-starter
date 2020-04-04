package com.muggle.poseidon.base;

import com.muggle.poseidon.entity.AuthUrlPathDO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-11-05
 **/

public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1553485005784190508L;
    private String message;
    private Integer code;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private ResultBean(String message, Integer code, T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    private ResultBean(String message,Integer code){
        this.code=code;
        this.message=message;
    }


    public  static <T> ResultBean<T> getInstance(String message,Integer code,T data){
        return new ResultBean<>(message,code,data);
    }

    public static <T> ResultBean<T> getInstance(String message,Integer code){
        return new ResultBean<>(message,code);
    }

    public static<T> ResultBean<T> success(){
        return new ResultBean<>("请求成功",200);
    }

    public static<T> ResultBean<T> success(String message){
        return new ResultBean<>(message,200);
    }

    public static<T> ResultBean<T> error(String message){
        return new ResultBean<>(message,500);
    }

    public static<T> ResultBean<T> error(String message,Integer code){
        return new ResultBean<>(message,code);
    }

    public static<T> ResultBean<T> successData(T data){
        return new ResultBean<>("请求成功",200,data);
    }


    public ResultBean() {

    }

}
