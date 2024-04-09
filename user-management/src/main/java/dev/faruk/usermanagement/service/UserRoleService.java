package dev.faruk.usermanagement.service;

import dev.faruk.commoncodebase.dto.UserRoleDTO;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserRoleService is the class that handles the business logic for the roles.
 */
@Service
public class UserRoleService {
    private final UserRepository userRepository;

    @Autowired
    public UserRoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns all the roles exist in the database.
     *
     * @return List of UserRoleDTO converted from AppUserRole entity set from database.
     */
    public List<UserRoleDTO> showRoles() {
        return userRepository.findRoles().stream().map(UserRoleDTO::new).toList();
    }

    /**
     * Returns the role with the given id.
     *
     * @param roleId id of the role
     * @return UserRoleDTO object converted from AppUserRole entity from database.
     */
    public UserRoleDTO showRoleById(Long roleId) {
        final AppUserRole role = userRepository.findRoleById(roleId);
        if (role == null) throw new AppHttpError.BadRequest("Role not found with id " + roleId);
        return new UserRoleDTO(role);
    }
}
