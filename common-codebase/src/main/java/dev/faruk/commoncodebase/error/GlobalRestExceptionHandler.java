package dev.faruk.commoncodebase.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

/**
 * GlobalRestExceptionHandler is the class that handles the exceptions thrown by the application and returns the
 * appropriate response to the client.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppHttpError.class)
    protected ResponseEntity<Object> handleException(AppHttpError e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(e.toJson());
    }
}
