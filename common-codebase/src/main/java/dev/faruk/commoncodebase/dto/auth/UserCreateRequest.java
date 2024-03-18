package dev.faruk.commoncodebase.dto.auth;

import lombok.*;

import java.util.List;

/**
 * RegisterRequest is the request payload type that should be used for register a user.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String username;
    private String password;
    private List<Long> roleIds;
}
