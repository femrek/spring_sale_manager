package dev.faruk.usermanagement.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.UserRoleDTO;
import dev.faruk.usermanagement.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserRoleController is the class that handles the requests for getting the roles.
 */
@RestController
@RequestMapping("/role")
public class UserRoleController {
    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping("/")
    public AppSuccessResponse<List<UserRoleDTO>> showRoles() {
        List<UserRoleDTO> roles = userRoleService.showRoles();
        return new AppSuccessResponse<>("All roles are listed successfully", roles);
    }

    @GetMapping("/{roleId}")
    public AppSuccessResponse<UserRoleDTO> showRole(@PathVariable Long roleId) {
        UserRoleDTO role = userRoleService.showRoleById(roleId);
        return new AppSuccessResponse<>("Role provided successfully", role);
    }
}
