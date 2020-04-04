package com.muggle.poseidon.annotation;

import java.lang.annotation.*;

/**
 * @description: 开启日志的注解，标注到方法上
 * @author: mozishu
 * @create: 2019-12-28 12:40
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceAction {
   String message() default "请求太频繁，请稍后再试";
   boolean Idempotent() default true;
   long expertime() default 3L;
}
