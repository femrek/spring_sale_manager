package dev.faruk.commoncodebase.aspect;

import dev.faruk.commoncodebase.feign.FeignExceptionMapper;
import dev.faruk.commoncodebase.feign.AuthorizeClient;
import feign.FeignException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This is an aspect for authorization of the requests. It checks the accessibility of the request path by sending
 * the request to the authorization server. If the request is not accessible, auth module throws an exception.
 */
@Aspect
@Component
public class AuthorizeAspect {
    private final AuthorizeClient authorizeClient;
    private final FeignExceptionMapper feignExceptionMapper;

    @Autowired
    public AuthorizeAspect(AuthorizeClient identifyClient, FeignExceptionMapper feignExceptionMapper) {
        this.authorizeClient = identifyClient;
        this.feignExceptionMapper = feignExceptionMapper;
    }

    @Pointcut("execution(* dev.faruk..*.controller..*(..))")
    private void controllerPointcut() {
    }

    @SuppressWarnings("AopLanguageInspection")
    @Pointcut("execution(* dev.faruk.auth.controller..*(..))")
    private void authControllerPointcut() {
    }

    /**
     * This advice is defined for all controller methods except the auth controller methods. It checks the accessibility
     * of the request path by sending the request to the authorization server. If the request is not accessible, auth
     * module throws an exception. The expected exception is handled by the {@link FeignExceptionMapper} and thrown to
     * the client.
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Before("controllerPointcut() && !authControllerPointcut()")
    public void getAllAdvice() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String path = request.getContextPath() + request.getServletPath();

        try {
            authorizeClient.checkAccessibility(authHeader, path);
        } catch (FeignException e) {
            throw feignExceptionMapper.map(e);
        }
    }
}
