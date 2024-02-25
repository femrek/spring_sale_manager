package dev.faruk.auth.dto;

import lombok.*;

/**
 * LoginResponse is the response type that should be used for login.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
}
