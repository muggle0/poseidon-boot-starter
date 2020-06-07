package com.muggle.poseidon.aop;

import com.github.pagehelper.Page;
import com.muggle.poseidon.base.BaseNormalQuery;
import com.muggle.poseidon.base.ResultBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/6/6
 **/

@Aspect
public class QueryAspect {

    private static final Log log = LogFactory.getLog(QueryAspect.class);

    @Pointcut("execution(* *.*Controller.*(com.muggle.poseidon.base.BaseNormalQuery+))")
    public void query() {
    }

    @Before("query()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length < 1) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof BaseNormalQuery) {
                BaseNormalQuery query = (BaseNormalQuery) arg;
                query.init();
                query.processSql();
            }
        }
    }

    @AfterReturning(pointcut = "query()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        if (result==null){
            return;
        }
        if (result instanceof ResultBean) {
            Object data = ((ResultBean) result).getData();
            if (data instanceof Page) {
                ((ResultBean) result).setTotal(((Page) data).getTotal());
            }
        }
    }
}
