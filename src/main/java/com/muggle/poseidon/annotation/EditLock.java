package com.muggle.poseidon.annotation;

/**
 * @author muggle
 */
public @interface EditLock {

    /**
     * 编辑锁提示信息
     */
    String message() default "资源被占用" ;

    /**
     * 资源锁key
     */
    String key();

    /**
     * 锁判断方法（反射）
     */
    String accessMethod();
}
