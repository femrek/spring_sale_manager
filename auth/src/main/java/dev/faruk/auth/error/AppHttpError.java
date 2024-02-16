package dev.faruk.auth.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

public abstract class AppHttpError extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final String message;
    private final Map<String, Object> errorMap;

    public AppHttpError(HttpStatusCode statusCode, String message, Map<String, Object> errorMap) {
        this.statusCode = statusCode;
        this.message = message;
        this.errorMap = errorMap;
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
            super(HttpStatus.BAD_REQUEST, message, errorMap);
        }
    }

    public static final class Unauthorized extends AppHttpError {
        public Unauthorized(String message) {
            this(message, null);
        }

        public Unauthorized(String message, Map<String, Object> errorMap) {
            super(HttpStatus.UNAUTHORIZED, message, errorMap);
        }
    }
}
