package dev.faruk.auth;

import dev.faruk.auth.dto.LoginResponse;
import dev.faruk.auth.service.AuthService;
import dev.faruk.auth.service.UserManagementService;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.repository.UserRepository;
import dev.faruk.auth.dto.LoginRequest;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.error.AppHttpError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AuthApplicationTests {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserManagementService userManagementService;

    @Autowired
    AuthApplicationTests(UserRepository userRepository,
                         AuthService authService,
                         UserManagementService userManagementService) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.userManagementService = userManagementService;
    }

    /**
     * This test registers a user, logs in with the registered user, and validates the token.
     * So, this test checks the main functionality of the auth service.
     */
    @Test
    void registerLoginValidate() {
        final UserCreateRequest testUserCredentials = new UserCreateRequest(
                "testUser",
                "testPassword",
                "Test User",
                List.of(1L, 2L));

        // delete the user if it exists
        if (userRepository.findByUsername(testUserCredentials.getUsername()) != null) {
            userRepository.deleteByUsername(testUserCredentials.getUsername());
        }

        // register the user
        final UserDTO registeredUser = userManagementService.createUser(testUserCredentials);

        // try to log in with wrong credentials
        final LoginRequest wrongLoginRequest = new LoginRequest(
                registeredUser.getUsername(),
                "wrongPassword");
        try {
            authService.generateToken(wrongLoginRequest);
            assert false;
        } catch (AppHttpError.Unauthorized ignored) {
        }

        // login with the registered user
        final LoginRequest loginRequest = new LoginRequest(
                testUserCredentials.getUsername(),
                testUserCredentials.getPassword());
        final LoginResponse token = authService.generateToken(loginRequest);

        // validate the token
        final UserDTO loggedInUser = authService.getUserByToken(token.getToken());

        // check if the logged-in user is the same as the registered user
        assert loggedInUser.getUsername().equals(testUserCredentials.getUsername());

        // delete the user even if the test fails
        userRepository.deleteByUsername(testUserCredentials.getUsername());
    }
}
