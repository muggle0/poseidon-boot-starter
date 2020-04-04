package com.muggle.poseidon.aop;

import com.muggle.poseidon.annotation.InterfaceAction;
import com.muggle.poseidon.base.PoseidonLocker;
import com.muggle.poseidon.base.exception.SimplePoseidonException;
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
    private PoseidonLocker locker;

    public LogAspect (PoseidonLocker locker){
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
        Enumeration<String> paramter = request.getParameterNames();
        StringBuilder stringBuilder = new StringBuilder();
        while (paramter.hasMoreElements()) {
            String str = (String) paramter.nextElement();
            stringBuilder.append(str + ":")
                    .append(request.getParameter(str) + "; ");
        }
        String userMessage = "用户名：%s";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getDetails() == null) {
            userMessage = String.format(userMessage, "用户未登陆");
        } else {
            userMessage = String.format(userMessage, ((UserDetails) authentication.getDetails()).getUsername());
        }

        log.info("》》》》》》 请求日志   "+userMessage+"url=" + request.getRequestURI() + "method=" + request.getMethod() + "ip=" + request.getRemoteAddr()
                + "host=" + request.getRemoteHost() + "port=" + request.getRemotePort()
                + "classMethod=" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
                + "paramters=" + stringBuilder.toString());
    }

    /**
     * 幂等操作
     * @param joinPoint
     */
    private void verifyIdempotent(JoinPoint joinPoint) {
        InterfaceAction annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(InterfaceAction.class);
        boolean idempotent = annotation.Idempotent();
        if (idempotent){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String requestURI = request.getRequestURI();
            String remoteAddr = request.getRemoteAddr();
            long expertime = annotation.expertime();
            boolean trylock = locker.trylock("[" + requestURI + "]:[" + remoteAddr + "]", expertime);
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

