package dev.faruk.commoncodebase.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * AppSuccessResponse is the only response type that should be used for successful requests.
 */
@Getter
@ToString
public class AppSuccessResponse<T> extends ResponseEntity<Map<String, Object>> {
    private final HttpStatusCode statusCode;
    private final String message;
    private final T data;

    public AppSuccessResponse(@Nullable T data) {
        this(HttpStatus.OK, "Success", data);
    }

    public AppSuccessResponse(@NonNull String message, @Nullable T data) {
        this(HttpStatus.OK, message, data);
    }

    public AppSuccessResponse(@NonNull HttpStatusCode status, @NonNull String message, @Nullable T data) {
        super(toJson(status, message, data), status);
        this.statusCode = status;
        this.message = message;
        this.data = data;
    }

    static private Map<String, Object> toJson(HttpStatusCode status, String message, Object data) {
        if (data == null) {
            return Map.of(
                    "status", status.value(),
                    "message", message
            );
        }
        return Map.of(
                "status", status.value(),
                "message", message,
                "data", data
        );
    }
}
