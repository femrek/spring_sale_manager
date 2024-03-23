package dev.faruk.commoncodebase.aspect;

import dev.faruk.commoncodebase.feign.FeignExceptionMapper;
import dev.faruk.commoncodebase.feign.IdentifyClient;
import feign.FeignException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthorizeAspect {
    private final IdentifyClient identifyClient;
    private final FeignExceptionMapper feignExceptionMapper;

    @Autowired
    public AuthorizeAspect(IdentifyClient identifyClient, FeignExceptionMapper feignExceptionMapper) {
        this.identifyClient = identifyClient;
        this.feignExceptionMapper = feignExceptionMapper;
    }

    @Pointcut("execution(* dev.faruk..*.controller..*(..))")
    public void controllerPointcut() {
    }

    @SuppressWarnings("AopLanguageInspection")
    @Pointcut("execution(* dev.faruk.auth.controller..*(..))")
    public void authControllerPointcut() {
    }

    @Before("controllerPointcut() && !authControllerPointcut()")
    public void getAllAdvice() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String path0 = request.getContextPath();
        final String path1 = request.getServletPath();
        System.out.println("BEFORE ASPECT");
        System.out.println("authHeader: " + authHeader);
        System.out.println("path0 " + path0);
        System.out.println("path1 " + path1);

        try {
            identifyClient.checkAccessibility(authHeader, path0 + path1);
        } catch (FeignException e) {
            throw feignExceptionMapper.map(e);
        }
    }
}
