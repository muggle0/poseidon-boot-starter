package com.muggle.poseidon.aop;

import com.muggle.poseidon.annotation.EditLock;
import com.muggle.poseidon.base.DistributedLocker;
import com.muggle.poseidon.base.exception.EditLockException;
import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;
import com.muggle.poseidon.base.exception.SimplePoseidonException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Description
 * Date 2021/5/12
 * Created by muggle
 */
@Aspect
public class EditLockAspect {

    private DistributedLocker locker;

    private ApplicationContext applicationContext;

    private static final Log log = LogFactory.getLog(RequestAspect.class);

    public EditLockAspect(DistributedLocker locker, ApplicationContext applicationContext) {
        this.locker = locker;
        this.applicationContext = applicationContext;
        log.info(">>>>>>>>>>>>>>>>>>>>>>>[编辑锁初始化]<<<<<<<<<<<<<<<<<<<<<");
    }

    @Pointcut("@annotation(com.muggle.poseidon.annotation.EditLock)")
    public void lockAccess() {
    }

    /**
     * 编辑前验证锁
     * @param joinPoint
     */
    @Before("lockAccess()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>> 编辑锁切面 dobefore <<<<<<<<<<<<<<<<<<<<<");
        EditLock annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(EditLock.class);
        String key = annotation.key();
        String accessMethod = annotation.accessMethod();
        // 验证方法 格式 userinfo#getuser
        String[] split = accessMethod.split("#");
        if (split.length != 2) {
            throw new EditLockException("编辑锁 accessMethod格式错误");
        }
        Object bean = applicationContext.getBean(split[0]);
        Object invoke=null;
        try {
            if (bean == null) {
                throw new EditLockException("编辑锁 accessMethod bean 名称错误");
            }
            Method declaredMethod = bean.getClass().getDeclaredMethod(split[0], String.class);
            invoke = declaredMethod.invoke(joinPoint.getArgs());
        } catch (Exception e) {
            new EditLockException("编辑锁 accessMethod M",e);
        }
        boolean result = locker.tryLock(key, invoke.toString(), 2, TimeUnit.HOURS);
        // 锁被其他编辑者占用，无法进入该方法编辑
        if (!result){
            throw new EditLockException(annotation.message());
        }
    }
}
