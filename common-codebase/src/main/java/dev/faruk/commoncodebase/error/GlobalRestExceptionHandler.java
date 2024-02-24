package dev.faruk.commoncodebase.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppHttpError.BadRequest.class)
    protected ResponseEntity<Object> handleException(AppHttpError.BadRequest e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.toJson());
    }

    @ExceptionHandler(AppHttpError.Unauthorized.class)
    protected ResponseEntity<Object> handleException(AppHttpError.Unauthorized e) {
        return ResponseEntity.status(UNAUTHORIZED).body(e.toJson());
    }

    @ExceptionHandler(AppHttpError.NotFound.class)
    protected ResponseEntity<Object> handleException(AppHttpError.NotFound e) {
        return ResponseEntity.status(NOT_FOUND).body(e.toJson());
    }
}
