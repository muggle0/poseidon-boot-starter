package com.muggle.poseidon.aop;

import javax.servlet.http.HttpServletRequest;

import com.muggle.poseidon.annotation.InterfaceAction;
import com.muggle.poseidon.base.DistributedLocker;
import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.base.exception.SimplePoseidonException;
import com.muggle.poseidon.handler.security.RequestLogProcessor;
import com.muggle.poseidon.util.RequestUtils;
import com.muggle.poseidon.util.UserInfoUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @description: 日志aop类
 * @author: muggle
 * @create: 2019-12-26 16:40
 */
@Aspect
public class RequestAspect {
    private DistributedLocker locker;

    private RequestLogProcessor logProcessor;

    public RequestAspect(DistributedLocker locker, RequestLogProcessor logProcessor) {
        this.logProcessor = logProcessor;
        this.locker = locker;
        log.info(">>>>>>>>>>>>>>>>>>>>>>>[请求日志切面初始化]<<<<<<<<<<<<<<<<<<<<<");
    }

    private static final Log log = LogFactory.getLog(RequestAspect.class);

    @Pointcut("@annotation(com.muggle.poseidon.annotation.InterfaceAction)")
    public void request() {
    }

    @Before("request()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>> 请求日志切面 dobefore <<<<<<<<<<<<<<<<<<<<<");
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
        try {
            UserDetails userInfo = UserInfoUtils.getUserInfo();
            userMessage = String.format(userMessage, userInfo == null ? "用户未登录" : userInfo.getUsername());
        } catch (BasePoseidonCheckException e) {
            userMessage = String.format(userMessage, "非法的登录用户");
        }
        log.info("》》》》》》 请求日志   " + userMessage + " url=" + request.getRequestURI() + "method=" + request.getMethod() + "ip=" + request.getRemoteAddr()
                + "host=" + request.getRemoteHost() + "port=" + request.getRemotePort()
                + "classMethod=" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
                + "paramters [ " + stringBuilder.toString() + " ]");
        if (logProcessor != null) {
            logProcessor.recordBefore(request, joinPoint.getArgs());
        }
    }

    /**
     * 幂等操作 @param joinPoint
     */
    private void verifyIdempotent(JoinPoint joinPoint) {
        InterfaceAction annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(InterfaceAction.class);
        boolean idempotent = annotation.Idempotent();
        if (idempotent) {
            log.debug(">>>>>>>>>>>>>>>>>>>>>>> 幂等操作 <<<<<<<<<<<<<<<<<<<<<");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String requestURI = request.getRequestURI();
            String remoteAddr = RequestUtils.getIP(request);
            log.info(String.format("请求进入幂等方法，开始尝试上锁 uri: %s ip:%s", requestURI, remoteAddr));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = "notSignin";
            if (authentication != null && authentication.getPrincipal() == null) {
                username = authentication.getPrincipal().toString();
            }
            String key = "lock:idempotent:" + request.getRequestURI() + ":" + username + ":" + RequestUtils.getIP(request);
            long expertime = annotation.expertime();
            log.info("冪等上锁 key :" + key);
            boolean trylock = false;
            try {
                trylock = locker.tryLock(key, expertime);
            } catch (InterruptedException e) {
                log.error(e);
            }
            if (!trylock) {
                String message = annotation.message();
                log.info(">>>>>>>>>>>>>>>>>>>>>> 获取幂等锁获取锁失败 key:" + key + "  <<<<<<<<<<<<<<<<<<<<<<<");
                throw new SimplePoseidonException(message);
            }
        }
    }


    @AfterReturning(pointcut = "request()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>> 操作日志，返回值切面执行  <<<<<<<<<<<<<<<<<<<<<<< ");
        if (logProcessor != null) {
            logProcessor.recordAfterReturning(result, joinPoint.getArgs());
        }
    }
}

