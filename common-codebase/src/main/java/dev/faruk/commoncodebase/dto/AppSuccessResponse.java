package dev.faruk.commoncodebase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * AppSuccessResponse is the only response type that should be used for successful requests.
 */
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppSuccessResponse<T>  {
    private final Integer status;
    private final String message;
    private final T data;

    public AppSuccessResponse(@Nullable T data) {
        this(HttpStatus.OK.value(), "Success", data);
    }

    public AppSuccessResponse(@NonNull String message, @Nullable T data) {
        this(HttpStatus.OK.value(), message, data);
    }

    public AppSuccessResponse(@NonNull Integer status, @NonNull String message, @Nullable T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    static private Map<String, Object> toJson(Integer status, String message, Object data) {
        if (data == null) {
            return Map.of(
                    "status", status,
                    "message", message
            );
        }
        return Map.of(
                "status", status,
                "message", message,
                "data", data
        );
    }
}
