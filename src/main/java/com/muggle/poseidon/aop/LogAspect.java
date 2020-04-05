package com.muggle.poseidon.aop;

import com.muggle.poseidon.annotation.InterfaceAction;
import com.muggle.poseidon.base.DistributedLocker;
import com.muggle.poseidon.base.exception.SimplePoseidonException;
import com.muggle.poseidon.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * @description: 日志aop类
 * @author: mozishu
 * @create: 2019-12-26 16:40
 */
@Aspect
public class LogAspect {
    private DistributedLocker locker;

    public LogAspect (DistributedLocker locker){
        this.locker=locker;
    }
    private static final Log log = LogFactory.getLog(LogAspect.class);

    @Pointcut("@annotation(com.muggle.poseidon.annotation.InterfaceAction)")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("===============aop doBefore===============");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        this.verifyIdempotent(joinPoint);
        //参数
        Object[] args = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : args) {
            stringBuilder.append(" (").append(arg.toString()).append(") ");
        }

        String userMessage = "用户名：%s";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            userMessage = String.format(userMessage, "用户未登陆");
        } else {
            userMessage = String.format(userMessage, authentication.getPrincipal().toString());
        }

        log.info("》》》》》》 请求日志   "+userMessage+"url=" + request.getRequestURI() + "method=" + request.getMethod() + "ip=" + request.getRemoteAddr()
                + "host=" + request.getRemoteHost() + "port=" + request.getRemotePort()
                + "classMethod=" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
                + "paramters [ " + stringBuilder.toString()+" ]");
    }

    /**
     * 幂等操作
     * @param joinPoint
     */
    private void verifyIdempotent(JoinPoint joinPoint) {
        InterfaceAction annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(InterfaceAction.class);
        boolean idempotent = annotation.Idempotent();
        log.debug("幂等操作》》》》》");
        if (idempotent){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String requestURI = request.getRequestURI();
            String remoteAddr = RequestUtils.getIP(request);
            log.info(String.format("请求进入幂等方法，开始尝试上锁 uri: %s ip:%s",requestURI,remoteAddr));
            long expertime = annotation.expertime();
            String key = annotation.key();
            boolean trylock = locker.trylock(key, expertime);
            if (!trylock){
                String message = annotation.message();
                throw new SimplePoseidonException(message);
            }
        }
    }
    @After("log()")
    public void doAfter() {
        log.debug("================aop doAfter==============");
    }
}

