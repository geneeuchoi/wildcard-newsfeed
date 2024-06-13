package com.sparta.wildcard_newsfeed.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j(topic = "UseTimeAop")
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect {

    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.comment.controller.*(..))")
    private void comment() {
    }

    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.liked.controller.*(..))")
    private void liked() {
    }

    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.post.controller.*(..))")
    private void post() {
    }

    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.token.controller.*(..))")
    private void token() {
    }

    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.user.controller.*(..))")
    private void user() {
    }

    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logBefore() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
    }
}
