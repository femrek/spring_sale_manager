package dev.faruk.commoncodebase.aspect;

import dev.faruk.commoncodebase.error.AppHttpError;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

/**
 * GlobalRestExceptionHandler is the class that handles the exceptions thrown by the application and returns the
 * appropriate response to the client.
 */
@Log4j2
@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppHttpError.class)
    protected ResponseEntity<Object> handleException(AppHttpError e) {
        log.debug("http error occurred: %s".formatted(e.getMessage()));
        return ResponseEntity.status(e.getStatusCode()).body(e.toJson());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        log.warn("An unhandled exception occurred: ", e);
        AppHttpError appHttpError = new AppHttpError.InternalServerError(e.getMessage());
        return ResponseEntity.status(appHttpError.getStatusCode()).body(appHttpError.toJson());
    }
}
