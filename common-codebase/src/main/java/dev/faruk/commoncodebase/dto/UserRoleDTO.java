package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.AppUserRole;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    private Long id;
    private String name;

    public UserRoleDTO(AppUserRole role) {
        this.id = role.getId();
        this.name = role.getName();
    }
}
