package dev.faruk.auth.service;

import dev.faruk.auth.dao.UserRepository;
import dev.faruk.auth.dto.request.LoginRequest;
import dev.faruk.auth.dto.request.RegisterRequest;
import dev.faruk.auth.dto.response.UserDTO;
import dev.faruk.auth.entity.AppUser;
import dev.faruk.auth.entity.AppUserRole;
import dev.faruk.auth.error.AppHttpError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * AuthService is the service class that should be used for authentication operations such as login, register, and token
 * validation.
 */
@Service
public class AuthService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Save the user with given credentials to the database
     * @param registerRequest the request object including the username, password and role list
     * @return the saved user
     */
    public AppUser saveUser(RegisterRequest registerRequest) {
        // create the user object from the request
        AppUser newUser = new AppUser();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        ArrayList<Integer> roleIds = new ArrayList<>();
        for (int roleId : registerRequest.getRoleIds()) {
            // check if the role is already added
            if (roleIds.contains(roleId)) {
                throw new AppHttpError.BadRequest(String.format("Role with id %d is duplicated", roleId));
            }
            roleIds.add(roleId);

            AppUserRole userRole = new AppUserRole();
            userRole.setId(roleId);
            newUser.addRole(userRole);
        }

        // save the created user
        return repository.save(newUser);
    }

    /**
     * Check if the given credentials are valid. throws {@link AppHttpError.Unauthorized} if the credentials are not
     * valid or the user is deleted or the user does not exist.
     * @param userCredentials the request object including the username and password
     */
    public void doesCredentialsValid(LoginRequest userCredentials) {
        AppUser userFromDatabase = repository.findByUsername(userCredentials.getUsername());
        if (userFromDatabase == null) {
            throw new AppHttpError.Unauthorized("User not found");
        }
        if (userFromDatabase.isDeleted()) {
            throw new AppHttpError.Unauthorized("User is deleted");
        }
        if (!passwordEncoder.matches(userCredentials.getPassword(), userFromDatabase.getPassword())) {
            throw new AppHttpError.Unauthorized("Password is incorrect");
        }
        // think about throwing the same error for all the cases
    }

    /**
     * Generate a token for the given username
     * @param username the username of the user
     * @return the generated token
     */
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    /**
     * Get the user by the given token
     * @param authHeader the token to get the user by
     * @return the user object
     */
    public UserDTO getUserByToken(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new AppHttpError.Unauthorized("Token is not provided");
        }
        if (authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }
        final String username = jwtService.getUsernameByToken(authHeader);
        final AppUser user = repository.findByUsername(username);
        return new UserDTO(user);
    }

    /**
     * Validate the given token. throws {@link AppHttpError.Unauthorized} if the token is not valid
     * @param authHeader the token to be validated
     */
    public void validateToken(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new AppHttpError.Unauthorized("Token is not provided");
        }
        if (authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }
        jwtService.validateToken(authHeader);
    }
}