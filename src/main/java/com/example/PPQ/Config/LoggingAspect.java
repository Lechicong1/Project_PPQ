package com.example.PPQ.Config;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.example.PPQ.ServiceImp..*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("↪  {}.{}() called with args: {}", className, methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("↩  {}.{}() returned [{}] in {} ms", className, methodName, result, duration);
            return result;
        } catch (Throwable ex) {
            log.error("💥 Exception in {}.{}(): {}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }
    }
}
