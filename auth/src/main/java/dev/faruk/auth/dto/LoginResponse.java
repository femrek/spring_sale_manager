package dev.faruk.auth.dto;

import dev.faruk.commoncodebase.logging.SensitiveDataType;
import lombok.*;

/**
 * LoginResponse is the response type that should be used for login.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements SensitiveDataType {
    private String token;

    @Override
    public String toVisualString() {
        return "LoginResponse{token='********'}";
    }

    @Override
    public String toString() {
        return toVisualString();
    }
}
