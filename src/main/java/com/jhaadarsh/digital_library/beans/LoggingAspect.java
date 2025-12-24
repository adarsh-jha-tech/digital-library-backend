package com.jhaadarsh.digital_library.beans;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // This advice runs before every method execution in the given package structure
    @Before("execution(* com.jhaadarsh.digital_library..*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        log.info("Logging: Calling {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
}
