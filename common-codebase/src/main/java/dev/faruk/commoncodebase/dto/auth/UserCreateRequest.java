package dev.faruk.commoncodebase.dto.auth;

import dev.faruk.commoncodebase.logging.SensitiveDataType;
import lombok.*;

import java.util.List;

/**
 * RegisterRequest is the request payload type that should be used for register a user.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest implements SensitiveDataType {
    private String username;
    private String password;
    private String name;
    private List<Long> roleIds;

    @Override
    public String toVisualString() {
        return new StringBuilder()
                .append("UserCreateRequest{")
                .append("username='").append(username).append('\'')
                .append(", password='").append("********").append('\'')
                .append(", name='").append(name).append('\'')
                .append(", roleIds=").append(roleIds).append('}').toString();
    }

    @Override
    public String toString() {
        return toVisualString();
    }
}
