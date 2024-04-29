package dev.faruk.auth.dto;

import lombok.*;

/**
 * LoginRequest is the request payload type that should be used for login.
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
