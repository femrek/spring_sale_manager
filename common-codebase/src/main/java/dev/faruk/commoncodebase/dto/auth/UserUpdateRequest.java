package dev.faruk.commoncodebase.dto.auth;

import lombok.*;

import java.util.List;

/**
 * UserUpdateRequest is the request payload type that should be used for updating a user. Compatible with PATCH method.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    /**
     * If password is provided, it will be updated. If not, it will not be updated.
     */
    private String password;

    /**
     * List of role ids that the user will have. This list have to include all the roles that the user will have.
     * If no list is provided, the user will have the same roles as before.
     */
    private List<Long> roleIds;

    public boolean isEmpty() {
        return password == null && roleIds == null;
    }
}
