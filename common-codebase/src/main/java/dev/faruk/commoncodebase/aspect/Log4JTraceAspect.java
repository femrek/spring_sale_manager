package dev.faruk.commoncodebase.aspect;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.logging.SensitiveDataType;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * An aspect for logging method calls with arguments and return values with Log4J2. This aspect only for trace level
 * logs.
 */
@Log4j2
@Aspect
@Component
public class Log4JTraceAspect {
    @Pointcut("execution(public * dev.faruk..*.controller..*.*(.., @org.springframework.web.bind.annotation.RequestBody (*), ..))")
    private void controllerWithRequestBodyPointcut() {
    }

    @Pointcut("execution(public * dev.faruk..*.controller..*.*(..)) && !controllerWithRequestBodyPointcut()")
    private void controllerWithoutRequestBodyPointcut() {
    }

    @Pointcut("@annotation(dev.faruk.commoncodebase.logging.IgnoreArgsLog4J2)")
    private void controllerWithRequestBodyAndIgnoredArgsPointcut() {
    }

    /**
     * This method is for logging controller methods with request body and ignored arguments.
     */
    @Around("controllerWithRequestBodyAndIgnoredArgsPointcut() && controllerWithRequestBodyPointcut()")
    public Object aroundControllerWithRequestBodyIgnored(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logController(joinPoint, null, false);
        }
        return joinPoint.proceed();
    }

    /**
     * This method is for logging controller methods with request body.
     */
    @Around("!controllerWithRequestBodyAndIgnoredArgsPointcut()" +
            " && controllerWithRequestBodyPointcut()" +
            " && (args(*, @org.springframework.web.bind.annotation.RequestBody arg, ..)" +
            " || args(.., @org.springframework.web.bind.annotation.RequestBody arg, *)" +
            " || args(@org.springframework.web.bind.annotation.RequestBody arg, ..)" +
            " || args(.., @org.springframework.web.bind.annotation.RequestBody arg))")
    public Object aroundControllerWithRequestBody(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logController(joinPoint, arg, true);
        }
        return joinPoint.proceed();
    }

    /**
     * This method is for logging controller methods without request body.
     */
    @Around("controllerWithoutRequestBodyPointcut()")
    public Object aroundControllerWithoutRequestBody(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logController(joinPoint, null, true);
        }
        return joinPoint.proceed();
    }

    private Object _logController(final ProceedingJoinPoint joinPoint,
                                  final Object body,
                                  boolean putArgs) throws Throwable {
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
        if (!putArgs) {
            log.trace("Controller: call: {}.{}(). args are hidden.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        } else if (argString == null) {
            log.trace("Controller: call: {}.{}() called with no body.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        } else {
            log.trace("Controller: call: {}.{}() called with body: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    argString);
        }

        // proceed
        final var result = joinPoint.proceed();

        // check null
        if (result == null) {
            log.warn("Controller: return: {}.{}() returned null.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            return null;
        }

        // log after
        if (putArgs) {
            final boolean isAppSuccessResponse = result instanceof ResponseEntity<?>
                    && ((ResponseEntity<?>) result).getBody() instanceof AppSuccessResponse<?>;
            log.trace("Controller: return: {}.{}() returned: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    isAppSuccessResponse ? ((ResponseEntity<?>) result).getBody() : result.toString());
        } else {
            log.trace("Controller: return: {}.{}(). result is hidden.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }

        // response
        return result;
    }

    @Pointcut("execution(* dev.faruk..*.service..*(..))")
    private void servicePointcut() {
    }

    @Pointcut("@annotation(dev.faruk.commoncodebase.logging.IgnoreArgsLog4J2)")
    private void ignoredArgsPointcut() {
    }

    @Around("servicePointcut() && !ignoredArgsPointcut()")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logService(joinPoint, true);
        }
        return joinPoint.proceed();
    }

    @Around("servicePointcut() && ignoredArgsPointcut()")
    public Object aroundServiceIgnoredArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.TRACE)) {
            return _logService(joinPoint, false);
        }
        return joinPoint.proceed();
    }

    private Object _logService(final ProceedingJoinPoint joinPoint, boolean putArgs) throws Throwable {
        // get arguments
        StringBuilder stringBuilder = new StringBuilder();
        if (putArgs) {
            for (Object arg : joinPoint.getArgs()) {
                if (arg == null) {
                    stringBuilder.append("null, ");
                } else if (arg instanceof SensitiveDataType) {
                    stringBuilder.append(((SensitiveDataType) arg).toVisualString()).append(", ");
                } else {
                    stringBuilder.append(arg).append(", ");
                }
            }
        }

        // log before
        if (putArgs) {
            log.trace("Service: call: {}.{}() called with args: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    stringBuilder.toString());
        } else {
            log.trace("Service: call: {}.{}(). args are hidden.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }

        // proceed
        final var result = joinPoint.proceed();

        // log after
        final boolean isResultNull = result == null;
        if (putArgs) {
            log.trace("Service: return: {}.{}() returned: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    isResultNull ? "null" : result.toString());
        } else {
            log.trace("Service: return: {}.{}(). result is hidden.",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }

        // response
        return result;
    }
}
