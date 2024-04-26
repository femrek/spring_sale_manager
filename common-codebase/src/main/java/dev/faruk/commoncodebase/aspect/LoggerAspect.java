package dev.faruk.commoncodebase.aspect;

import dev.faruk.commoncodebase.dto.log.AppHttpErrorLogCreateDTO;
import dev.faruk.commoncodebase.dto.log.ErrorLogCreateDTO;
import dev.faruk.commoncodebase.dto.log.LogRequestDTO;
import dev.faruk.commoncodebase.dto.log.SuccessLogCreateDTO;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.logging.LogService;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Objects;

/**
 * This is an aspect for logging the requests and responses. It logs the request and response of the controller methods.
 */
@Aspect
@Component
public class LoggerAspect {
    private final LogService logService;

    @Autowired
    public LoggerAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(public * dev.faruk..*.controller..*.*(..))")
    private void controllersPointcut() {
    }

    @Pointcut("execution(public * dev.faruk..*.controller..*.*(@org.springframework.web.bind.annotation.RequestBody (*), ..))")
    private void controllerWithRequestBodyPointcut() {
    }

    @Pointcut("@annotation(dev.faruk.commoncodebase.logging.IgnoreLog)")
    private void ignoreLogPointcut() {
    }

    @Pointcut("controllerWithRequestBodyPointcut() && !ignoreLogPointcut()")
    private void logWithRequestBodyPointcut() {
    }

    @Pointcut("controllersPointcut() && !ignoreLogPointcut()")
    private void logAnyPointcut() {
    }

    /**
     * This advice is defined for all controller methods that have a request body.
     * @param joinPoint ProceedingJoinPoint object
     * @param requestBody Request body of the controller method
     * @return the response
     * @throws Throwable if an error occurs when proceeding the join point
     */
    @Order
    @Around("logWithRequestBodyPointcut() && args(@org.springframework.web.bind.annotation.RequestBody requestBody, ..)")
    public Object logWithRequestBody(ProceedingJoinPoint joinPoint, @RequestBody Object requestBody) throws Throwable {
        LogRequestDTO logRequestDTO = LogRequestDTO.fromContext(requestBody);
        return log(joinPoint, logRequestDTO);
    }

    /**
     * This advice is defined for all controller methods that do not have a request body.
     * @param joinPoint ProceedingJoinPoint object
     * @return the response
     * @throws Throwable if an error occurs when proceeding the join point
     */
    @Order
    @Around("logAnyPointcut() && !controllerWithRequestBodyPointcut()")
    public Object logWithoutRequestBody(ProceedingJoinPoint joinPoint) throws Throwable {
        LogRequestDTO logRequestDTO = LogRequestDTO.fromContext(null);
        return log(joinPoint, logRequestDTO);
    }

    private Object log(ProceedingJoinPoint joinPoint, LogRequestDTO logRequestDTO) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getResponse();
            Integer statusCode = Objects.requireNonNull(response).getStatus();
            SuccessLogCreateDTO successLogCreateDTO = SuccessLogCreateDTO.builder()
                    .statusCode(statusCode)
                    .request(logRequestDTO)
                    .response(result)
                    .responseTime(new Date())
                    .build();
            logService.saveLog(successLogCreateDTO);
            return result;
        } catch (AppHttpError e) {
            AppHttpErrorLogCreateDTO appHttpErrorLogCreateDTO = AppHttpErrorLogCreateDTO.builder()
                    .request(logRequestDTO)
                    .error(e)
                    .responseTime(new Date())
                    .build();
            logService.saveLog(appHttpErrorLogCreateDTO);
            throw e;
        } catch (Exception e) {
            ErrorLogCreateDTO errorLogCreateDTO = ErrorLogCreateDTO.builder()
                    .request(logRequestDTO)
                    .error(e.getMessage())
                    .stackTrace(e.getStackTrace())
                    .responseTime(new Date())
                    .build();
            logService.saveLog(errorLogCreateDTO);
            throw e;
        }
    }
}
