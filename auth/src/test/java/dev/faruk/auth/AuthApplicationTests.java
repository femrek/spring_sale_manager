package dev.faruk.auth;

import dev.faruk.auth.controller.AuthController;
import dev.faruk.auth.dao.UserRepository;
import dev.faruk.auth.dto.request.LoginRequest;
import dev.faruk.auth.dto.request.RegisterRequest;
import dev.faruk.auth.dto.response.UserDTO;
import dev.faruk.auth.error.AppHttpError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AuthApplicationTests {
    private final UserRepository userRepository;
    private final AuthController authController;

    @Autowired
    AuthApplicationTests(UserRepository userRepository, AuthController authController) {
        this.userRepository = userRepository;
        this.authController = authController;
    }

    /**
     * This test registers a user, logs in with the registered user, and validates the token.
     * So, this test checks the main functionality of the auth service.
     */
    @Test
    void registerLoginValidate() {
        final RegisterRequest testUserCredentials = new RegisterRequest(
                "testUser",
                "testPassword",
                List.of(1, 2));

        // delete the user if it exists
        if (userRepository.findByUsername(testUserCredentials.getUsername()) != null) {
            userRepository.deleteByUsername(testUserCredentials.getUsername());
        }

        // register the user
        final UserDTO registeredUser = authController.register(testUserCredentials).getData();

        // try to log in with wrong credentials
        final LoginRequest wrongLoginRequest = new LoginRequest(
                registeredUser.getUsername(),
                "wrongPassword");
        try {
            authController.login(wrongLoginRequest);
            assert false;
        } catch (AppHttpError.Unauthorized ignored) {}

        // login with the registered user
        final LoginRequest loginRequest = new LoginRequest(
                testUserCredentials.getUsername(),
                testUserCredentials.getPassword());
        final String token = authController.login(loginRequest).getData();

        // validate the token
        final UserDTO loggedInUser = authController.user(token).getData();

        // check if the logged-in user is the same as the registered user
        assert loggedInUser.getUsername().equals(testUserCredentials.getUsername());
    }
}
