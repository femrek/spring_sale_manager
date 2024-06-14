package dev.faruk.auth.service;

import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Log4j2
@Service
public class UserManagementService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save the user with given credentials to the database
     *
     * @param registerRequest the request object including the username, password and role list
     * @return the saved user
     */
    public UserDTO createUser(UserCreateRequest registerRequest) {
        // check if the username is already taken
        AppUser userWithGivenUsername = userRepository.findByUsername(registerRequest.getUsername());
        if (userWithGivenUsername != null) {
            log.debug("the user with the given username was exist when creating a user. username: '%s'. user marked as deleted: %s"
                    .formatted(userWithGivenUsername.getUsername(), userWithGivenUsername.getDeleted()));
            throw new AppHttpError.BadRequest(
                    String.format("Username %s is already taken", registerRequest.getUsername()));
        }

        // create the user object from the request
        AppUser newUser = new AppUser();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setName(registerRequest.getName());
        ArrayList<Long> roleIds = new ArrayList<>();
        for (Long roleId : registerRequest.getRoleIds()) {
            // check if the role is already added
            if (roleIds.contains(roleId)) {
                log.debug("the role with the given id was duplicated when creating a user. request: %s"
                        .formatted(registerRequest.toVisualString()));
                throw new AppHttpError.BadRequest(String.format("Role with id %d is duplicated", roleId));
            }
            roleIds.add(roleId);

            AppUserRole userRole = userRepository.findRoleById(roleId);
            newUser.add(userRole);
        }

        // save the created user
        return new UserDTO(userRepository.create(newUser));
    }

    /**
     * Update the user with the given id
     *
     * @param id                the id of the user to be updated
     * @param userUpdateRequest the request object including the new user data
     * @return the updated user
     */
    public UserDTO updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        AppUser user = userRepository.findById(id);
        if (user == null || user.getDeleted()) {
            log.debug("the user with the given id was not found when updating a user. id: %d".formatted(id));
            throw new AppHttpError.NotFound(String.format("User not found with id %d", id));
        }
        if (userUpdateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }
        if (userUpdateRequest.getName() != null) {
            user.setName(userUpdateRequest.getName());
        }
        if (userUpdateRequest.getRoleIds() != null) {
            user.getRoles().clear();
            for (Long roleId : userUpdateRequest.getRoleIds()) {
                // check if the role is already added
                if (user.getRoles().stream().anyMatch(role -> role.getId().equals(roleId))) {
                    log.debug("the role with the given id was duplicated when updating a user. request: %s"
                            .formatted(userUpdateRequest.toVisualString()));
                    throw new AppHttpError.BadRequest(String.format("Role with id %d is duplicated", roleId));
                }

                AppUserRole userRole = userRepository.findRoleById(roleId);
                user.add(userRole);
            }
        }
        return new UserDTO(userRepository.update(user));
    }

    /**
     * Delete the user with the given id. This method marks the user as deleted.
     *
     * @param id the id of the user to be deleted
     */
    public void deleteUser(Long id) {
        final AppUser user = userRepository.findOnlyExistById(id);
        if (user == null || user.getDeleted()) {
            if (user == null) {
                log.debug("the user with the given id was not found (even if marked as deleted) when deleting a user. id: %d"
                        .formatted(id));
            } else {
                log.debug("the user with the given id was already deleted when deleting a user. id: %d".formatted(id));
            }
            throw new AppHttpError.BadRequest(String.format("User not found with id %d", id));
        }
        userRepository.deleteSoft(user);
    }
}
