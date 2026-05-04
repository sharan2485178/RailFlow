package com.example.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class AuditLogger {
	
	@Pointcut("execution(* com.example.controller.*.*(..))")
	public void controllerLayer() {}
	
	@Pointcut("execution(* com.example.service.*.*(..))")
	public void serviceLayer() {}
	
	@Before("serviceLayer()")
	public void logBefore(JoinPoint jp) {
		String method=jp.getSignature().getName();
		
		Object[] args=jp.getArgs();
		
		String className=jp.getTarget().getClass().getSimpleName();
		
		log.info("[BEFORE] [{}.{}()] | args: {}",
                className, method, Arrays.toString(args));
	}
	
	@AfterReturning(pointcut="serviceLayer()",returning="result")
	public void lofAfterReturning(JoinPoint jp,Object result)
	{
		String method = jp.getSignature().getName();
        String className = jp.getTarget().getClass().getSimpleName();

        log.info("[SUCCESS] [{}.{}()] | returned: {}",
                  className, method, result);
	}
	
	@AfterThrowing(pointcut="serviceLayer()",throwing="ex")
	public void logAfterThrowing(JoinPoint jp, Exception ex) {

        String method = jp.getSignature().getName();
        String className = jp.getTarget().getClass().getSimpleName();

        log.error("[EXCEPTION] [{}.{}()] | error: {} | type: {}",
                   className, method,
                   ex.getMessage(),
                   ex.getClass().getSimpleName());
    }
	
	@After("serviceLayer()")
    public void logAfter(JoinPoint jp) {

        String method = jp.getSignature().getName();
        String className = jp.getTarget().getClass().getSimpleName();

        log.info("[AFTER] [{}.{}()] | execution finished",
                  className, method);
    }
	

}
