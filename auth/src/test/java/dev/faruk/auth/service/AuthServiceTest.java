package dev.faruk.auth.service;

import dev.faruk.auth.AuthTestConfiguration;
import dev.faruk.auth.AuthTestDataSource;
import dev.faruk.auth.dto.LoginRequest;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.error.AppHttpError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthTestConfiguration.class)
class AuthServiceTest {
    private final AuthService authService;
    private final AuthTestDataSource authTestDataSource;

    @Autowired
    public AuthServiceTest(AuthService authService,
                           AuthTestDataSource authTestDataSource) {
        this.authService = authService;
        this.authTestDataSource = authTestDataSource;
    }

    @BeforeEach
    void setUp() {
        authTestDataSource.cleanDatabase();
        authTestDataSource.loadTestDataSetToDatabase();
    }

    @AfterEach
    void tearDown() {
        authTestDataSource.cleanDatabase();
    }

    @Test
    void doesCredentialsValid_validCredentials() {
        authService.doesCredentialsValid(LoginRequest.builder().username(AuthTestDataSource.ADMIN_USERNAME).password(AuthTestDataSource.PASSWORD).build());
    }

    @Test
    void doesCredentialsValid_invalidCredentials() {
        assertThrows(AppHttpError.Unauthorized.class, () -> authService.doesCredentialsValid(LoginRequest.builder()
                .username(AuthTestDataSource.ADMIN_USERNAME)
                .password("wrongPassword")
                .build()));
    }

    @Test
    void doesCredentialsValid_deletedUser() {
        assertThrows(AppHttpError.Unauthorized.class, () -> authService.doesCredentialsValid(LoginRequest.builder()
                .username(AuthTestDataSource.DELETED_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()));
    }

    @Test
    void doesCredentialsValid_userNotExists() {
        assertThrows(AppHttpError.Unauthorized.class, () -> authService.doesCredentialsValid(LoginRequest.builder()
                .username("userNotExists")
                .password(AuthTestDataSource.PASSWORD)
                .build()));
    }

    @Test
    void generateToken_notNull() {
        String token = authService.generateToken(LoginRequest.builder()
                .username(AuthTestDataSource.ADMIN_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()).getToken();
        assertNotNull(token);
    }

    @Test
    void getUserByToken_onlyTokenParameter() {
        String token = authService.generateToken(LoginRequest.builder()
                .username(AuthTestDataSource.ADMIN_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()).getToken();
        UserDTO userFromToken = authService.getUserByToken(token);

        assertNotNull(userFromToken);
    }

    @Test
    void getUserByToken_authHeaderParameter() {
        String token = authService.generateToken(LoginRequest.builder()
                .username(AuthTestDataSource.ADMIN_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()).getToken();
        UserDTO userFromToken = authService.getUserByToken("Bearer " + token);

        assertNotNull(userFromToken);
    }

    @Test
    void getUserByToken_existingUser_admin() {
        String token = authService.generateToken(LoginRequest.builder()
                .username(AuthTestDataSource.ADMIN_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()).getToken();
        UserDTO userFromToken = authService.getUserByToken(token);

        assertNotNull(userFromToken);
        assertEquals(AuthTestDataSource.ADMIN_USERNAME, userFromToken.getUsername());
        assertTrue(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")));
        assertFalse(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("CASHIER")));
        assertFalse(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("MANAGER")));
        assertEquals(1, userFromToken.getRoles().size());
    }

    @Test
    void getUserByToken_existingUser_cashier() {
        String token = authService.generateToken(LoginRequest.builder()
                .username(AuthTestDataSource.CASHIER_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()).getToken();
        UserDTO userFromToken = authService.getUserByToken(token);

        assertNotNull(userFromToken);
        assertEquals(AuthTestDataSource.CASHIER_USERNAME, userFromToken.getUsername());
        assertFalse(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")));
        assertTrue(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("CASHIER")));
        assertFalse(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("MANAGER")));
        assertEquals(1, userFromToken.getRoles().size());
    }

    @Test
    void getUserByToken_existingUser_manager() {
        String token = authService.generateToken(LoginRequest.builder()
                .username(AuthTestDataSource.MANAGER_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .build()).getToken();
        UserDTO userFromToken = authService.getUserByToken(token);

        assertNotNull(userFromToken);
        assertEquals(AuthTestDataSource.MANAGER_USERNAME, userFromToken.getUsername());
        assertFalse(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")));
        assertFalse(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("CASHIER")));
        assertTrue(userFromToken.getRoles().stream().anyMatch(role -> role.getName().equals("MANAGER")));
        assertEquals(1, userFromToken.getRoles().size());
    }

    @Test
    void getUserByToken_deletedUser() {
        assertThrows(AppHttpError.Unauthorized.class, () -> authService.getUserByToken(
                authService.generateToken(LoginRequest.builder()
                        .username(AuthTestDataSource.DELETED_USERNAME)
                        .password(AuthTestDataSource.PASSWORD)
                        .build()).getToken()));
    }
}