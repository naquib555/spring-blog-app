package com.blog.app.handler.aop;

import com.blog.app.common.util.LoggerUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingHandler {

    private final static Logger LOGGER_REQ_RES = LoggerUtil.getLogger(LoggerUtil.LogType.REQ_RES_LOG);

    @Pointcut("within(org.springframework.web.bind.annotation.RestController)")
    void restController() {

    }


    @Pointcut("execution(* *.*(..))")
    protected void allMethod() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    protected void postAction() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    protected void getAction() {

    }

    @Before("postAction()")
    void logPostActionBefore(JoinPoint joinPoint) {
        Class clazz = joinPoint.getTarget().getClass();

        String url = getPostRequestUrl(joinPoint, clazz);
        String payload = getPayload(joinPoint);

        LOGGER_REQ_RES.info(String.format("POST: %s || Request Payload: %s", url, payload));
    }

    @Before("getAction()")
    void logGetActionBefore(JoinPoint joinPoint) {
        Class clazz = joinPoint.getTarget().getClass();

        String url = getGetRequestUrl(joinPoint, clazz);
        String payload = getPayload(joinPoint);

        LOGGER_REQ_RES.info(String.format("GET: %s || Request Payload: %s", url, payload));
    }


    @AfterReturning(pointcut = "postAction()", returning = "result")
    public void logPostActionAfterReturning(JoinPoint joinPoint, Object result) {
        Class clazz = joinPoint.getTarget().getClass();

        String url = getPostRequestUrl(joinPoint, clazz);

        LOGGER_REQ_RES.info(String.format("POST: %s || Response Payload: %s", url, result));
    }

    @AfterReturning(pointcut = "getAction()", returning = "result")
    public void logGetActionAfterReturning(JoinPoint joinPoint, Object result) {
        Class clazz = joinPoint.getTarget().getClass();

        String url = getGetRequestUrl(joinPoint, clazz);

        LOGGER_REQ_RES.info(String.format("GET: %s || Response Payload: %s", url, result));
    }

    @Around("restController() && allMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            LOGGER_REQ_RES.debug("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            return result;
        } catch (IllegalArgumentException e) {
            LOGGER_REQ_RES.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }

    private String getPostRequestUrl(JoinPoint joinpoint, Class clazz) {
        MethodSignature methodSignature = (MethodSignature) joinpoint.getSignature();
        Method method = methodSignature.getMethod();

        PostMapping methodPostMapping = method.getAnnotation(PostMapping.class);

        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);

        return getPostUrl(requestMapping, methodPostMapping);
    }

    private String getGetRequestUrl(JoinPoint joinpoint, Class clazz) {
        MethodSignature methodSignature = (MethodSignature) joinpoint.getSignature();
        Method method = methodSignature.getMethod();

        GetMapping methodPostMapping = method.getAnnotation(GetMapping.class);

        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);

        return getGetUrl(requestMapping, methodPostMapping);
    }

    private String getPayload(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            String parameterName = signature.getParameterNames()[i];
            stringBuilder.append(parameterName);
            stringBuilder.append(joinPoint.getArgs()[i].toString());
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    private String getPostUrl(RequestMapping requestMapping, PostMapping postMapping) {
        String baseUrl = getUrl(requestMapping.value());
        String endpoint = getUrl(postMapping.value());

        return baseUrl + endpoint;
    }

    private String getGetUrl(RequestMapping requestMapping, GetMapping getMapping) {
        String baseUrl = getUrl(requestMapping.value());
        String endpoint = getUrl(getMapping.value());

        return baseUrl + endpoint;
    }

    private String getUrl(String[] value) {
        if(value.length == 0) return "";
        else return value[0];
    }

}
