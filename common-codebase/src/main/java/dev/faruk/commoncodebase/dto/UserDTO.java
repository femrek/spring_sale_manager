package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import lombok.*;

import java.util.ArrayList;
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
    private Long id;
    private String username;
    private List<UserRoleDTO> roles;

    public UserDTO(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        roles = new ArrayList<>();
        for (AppUserRole role : user.getRoles()) {
            roles.add(new UserRoleDTO(role));
        }
    }
}
