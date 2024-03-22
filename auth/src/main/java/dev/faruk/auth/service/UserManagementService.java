package dev.faruk.auth.service;

import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
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
                throw new AppHttpError.BadRequest(String.format("Role with id %d is duplicated", roleId));
            }
            roleIds.add(roleId);

            AppUserRole userRole = new AppUserRole();
            userRole.setId(roleId);
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
        AppUser user = userRepository.findOnlyExistById(id);
        if (user == null) {
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
                AppUserRole userRole = new AppUserRole();
                userRole.setId(roleId);
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
        if (user == null) {
            throw new AppHttpError.BadRequest(String.format("User not found with id %d", id));
        }
        userRepository.deleteSoftById(user);
    }
}
