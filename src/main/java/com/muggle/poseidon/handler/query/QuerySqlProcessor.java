package com.muggle.poseidon.handler.query;

import com.muggle.poseidon.base.BaseQuery;
import com.muggle.poseidon.base.ResultBean;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/6/8
 **/
public interface QuerySqlProcessor {

    /**
     * 返回值处理逻辑
     * @param query
     */
    void afterReturningQuery(ResultBean query);

    /**
     * aop执行，调用自定义query处理逻辑
     * @param query
     */
    void beforeQuery(BaseQuery query);


}
