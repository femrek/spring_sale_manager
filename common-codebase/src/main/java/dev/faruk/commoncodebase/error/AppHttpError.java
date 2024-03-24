package dev.faruk.commoncodebase.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Getter
public abstract class AppHttpError extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final String message;
    private final Map<String, Object> errorMap;

    public AppHttpError(HttpStatusCode statusCode, String message, Map<String, Object> errorMap) {
        this.statusCode = statusCode;
        this.message = message;
        this.errorMap = errorMap;
    }

    static public AppHttpError byStatusCode(HttpStatus statusCode, String message) {
        return switch (statusCode) {
            case BAD_REQUEST -> new BadRequest(message);
            case UNAUTHORIZED -> new Unauthorized(message);
            case FORBIDDEN -> new Forbidden(message);
            case NOT_FOUND -> new NotFound(message);
            default -> new InternalServerError(message);
        };
    }

    public Map<String, Object> toJson() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", statusCode.value());
        result.put("message", message);
        if (errorMap != null) result.put("errors", errorMap);
        return result;
    }

    public static final class BadRequest extends AppHttpError {
        public BadRequest(String message) {
            this(message, null);
        }

        public BadRequest(String message, Map<String, Object> errorMap) {
            super(BAD_REQUEST, message, errorMap);
        }
    }

    public static final class Unauthorized extends AppHttpError {
        public Unauthorized(String message) {
            this(message, null);
        }

        public Unauthorized(String message, Map<String, Object> errorMap) {
            super(UNAUTHORIZED, message, errorMap);
        }
    }

    public static final class Forbidden extends AppHttpError {
        public Forbidden(String message) {
            this(message, null);
        }

        public Forbidden(String message, Map<String, Object> errorMap) {
            super(FORBIDDEN, message, errorMap);
        }
    }

    public static final class NotFound extends AppHttpError {
        public NotFound(String message) {
            this(message, null);
        }

        public NotFound(String message, Map<String, Object> errorMap) {
            super(NOT_FOUND, message, errorMap);
        }
    }

    public static final class InternalServerError extends AppHttpError {
        public InternalServerError(String message) {
            this(message, null);
        }

        public InternalServerError(String message, Map<String, Object> errorMap) {
            super(INTERNAL_SERVER_ERROR, message, errorMap);
        }
    }
}
