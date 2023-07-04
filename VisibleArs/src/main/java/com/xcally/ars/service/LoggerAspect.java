package com.xcally.ars.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Around("execution(* com.xcally.ars..*Controller.*(..)) || execution(* com.xcally.ars..*Service.*(..)) || execution(* com.xcally.ars..*Mapper.*(..))")
	//@Around("execution(* com.xcally.ars..*Controller.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		StringBuilder parameterLog = new StringBuilder();
	    String name = joinPoint.getSignature().getDeclaringTypeName();
	    String type =
	            StringUtils.contains(name, "Controller") ? "Controller ===> " :
	                    StringUtils.contains(name, "Service") ? "Service ===> " :
	                            StringUtils.contains(name, "Mapper") ? "Mapper ===> " :
	                                    "";

	    //log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");

	    // Parameter logging
	    Object[] args = joinPoint.getArgs();
	    if (args != null && args.length > 0) {
	        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
	        String[] parameterNames = methodSignature.getParameterNames();
	        if (parameterNames != null) {
	            
	            for (int i = 0; i < parameterNames.length; i++) {
	                parameterLog.append(parameterNames[i]).append("=").append(args[i]).append(", ");
	            }
	            //log.debug(parameterLog.toString());
	        }
	    }
	    log.debug(type + name + "." + joinPoint.getSignature().getName() + "()" +"&& Parameters ===> "+ parameterLog.toString());

	    return joinPoint.proceed();
    }

}