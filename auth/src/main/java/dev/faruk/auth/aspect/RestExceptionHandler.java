package dev.faruk.auth.aspect;

import dev.faruk.auth.error.AppHttpError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppHttpError.BadRequest.class)
    protected ResponseEntity<Object> handleEntityNotFound(AppHttpError.BadRequest e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.toJson());
    }

    @ExceptionHandler(AppHttpError.Unauthorized.class)
    protected ResponseEntity<Object> handleEntityNotFound(AppHttpError.Unauthorized e) {
        return ResponseEntity.status(UNAUTHORIZED).body(e.toJson());
    }
}
