package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import lombok.*;

import java.util.List;

/**
 * UserDTO is the response payload type that should be used for user details.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String username;
    private List<AppUserRole> roles;

    public UserDTO(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }
}
