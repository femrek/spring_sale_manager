package dev.faruk.auth.dto;

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
public class RegisterRequest {
    private String username;
    private String password;
    private List<Integer> roleIds;
}
