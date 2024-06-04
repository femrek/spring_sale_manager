package dev.faruk.commoncodebase.aspect;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.logging.SensitiveDataType;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * An aspect for logging method calls with arguments and return values with Log4J2. This aspect only for trace level
 * logs.
 */
@Log4j2
@Aspect
@Component
public class Log4JTrackAspect {
    @Pointcut("execution(public * dev.faruk..*.controller..*.*(.., @org.springframework.web.bind.annotation.RequestBody (*), ..))")
    private void controllerWithRequestBodyPointcut() {
    }

    @Pointcut("execution(public * dev.faruk..*.controller..*.*(..)) && !controllerWithRequestBodyPointcut()")
    private void controllerWithoutRequestBodyPointcut() {
    }

    @Around("controllerWithRequestBodyPointcut() && args(*, @org.springframework.web.bind.annotation.RequestBody arg, ..)")
    public Object aroundControllerWithRequestBody(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logController(joinPoint, arg);
        }
        return joinPoint.proceed();
    }

    @Around("controllerWithoutRequestBodyPointcut()")
    public Object aroundControllerWithoutRequestBody(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logController(joinPoint, null);
        }
        return joinPoint.proceed();
    }

    private Object _logController(final ProceedingJoinPoint joinPoint, final Object body) throws Throwable {
        // get arguments
        String argString;
        if (body == null) {
            argString = null;
        } else if (body instanceof SensitiveDataType) {
            argString = ((SensitiveDataType) body).toVisualString();
        } else {
            argString = body.toString();
        }

        // log before
        if (argString == null) {
            log.trace("{}.{}() called with no body.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        } else {
            log.trace("{}.{}() called with body: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    argString);
        }

        // proceed
        final var result = joinPoint.proceed();

        // check null
        if (result == null) {
            log.warn("{}.{}() returned null.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            return null;
        }

        // log after
        final boolean isAppSuccessResponse = result instanceof AppSuccessResponse<?>;
        log.trace("Controller: {}.{}() returned: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                isAppSuccessResponse ? result : result.toString());

        // response
        return result;
    }

    @Pointcut("execution(* dev.faruk..*.service..*(..))")
    private void servicePointcut() {
    }

    @Around("servicePointcut()")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logService(joinPoint);
        }
        return joinPoint.proceed();
    }

    private Object _logService(final ProceedingJoinPoint joinPoint) throws Throwable {
        // get arguments
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg == null) {
                stringBuilder.append("null, ");
            } else if (arg instanceof SensitiveDataType) {
                stringBuilder.append(((SensitiveDataType) arg).toVisualString()).append(", ");
            } else {
                stringBuilder.append(arg).append(", ");
            }
        }

        // log before
        log.trace("{}.{}() called with args: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                stringBuilder.toString());

        // proceed
        final var result = joinPoint.proceed();

        // log after
        final boolean isResultNull = result == null;
        log.trace("Service: {}.{}() returned: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                isResultNull ? "null" : result.toString());

        // response
        return result;
    }
}
