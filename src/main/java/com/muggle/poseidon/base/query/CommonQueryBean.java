package com.muggle.poseidon.base.query;

import java.util.Map;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/5/20
 **/
public class CommonQueryBean<T>  {
    private T data;

    private Map<String,String> operator;

    private String orderBy ;

    private int start;

    private int pageSize;

    private String groupBy;

    public String getSql(){
        return null;
    }
}
