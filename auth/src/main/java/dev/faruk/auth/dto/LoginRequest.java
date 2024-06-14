package dev.faruk.auth.dto;

import dev.faruk.commoncodebase.logging.SensitiveDataType;
import lombok.*;

/**
 * LoginRequest is the request payload type that should be used for login.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements SensitiveDataType {
    private String username;
    private String password;

    @Override
    public String toVisualString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoginRequest{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append("********").append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String toString() {
        return toVisualString();
    }
}
