package dev.faruk.auth.service;

import dev.faruk.auth.AuthTestConfiguration;
import dev.faruk.auth.AuthTestDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthTestConfiguration.class)
class JwtServiceTest {
    private final JwtService jwtService;
    private final AuthTestDataSource authTestDataSource;

    @Autowired
    public JwtServiceTest(JwtService jwtService,
                          AuthTestDataSource authTestDataSource) {
        this.jwtService = jwtService;
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
    void generateToken_then_getUsernameByToken_userAdmin() {
        String token = jwtService.generateToken(AuthTestDataSource.ADMIN_USERNAME);
        assertNotNull(token);

        String username = jwtService.getUsernameByToken(token);
        assertEquals(AuthTestDataSource.ADMIN_USERNAME, username);
    }

    @Test
    void generateToken_then_getUsernameByToken_userCashier() {
        String token = jwtService.generateToken(AuthTestDataSource.CASHIER_USERNAME);
        assertNotNull(token);

        String username = jwtService.getUsernameByToken(token);
        assertEquals(AuthTestDataSource.CASHIER_USERNAME, username);
    }

    @Test
    void generateToken_then_getUsernameByToken_userManager() {
        String token = jwtService.generateToken(AuthTestDataSource.MANAGER_USERNAME);
        assertNotNull(token);

        String username = jwtService.getUsernameByToken(token);
        assertEquals(AuthTestDataSource.MANAGER_USERNAME, username);
    }

    @Test
    void generateToken_then_getUsernameByToken_userDeleted() {
        String token = jwtService.generateToken(AuthTestDataSource.DELETED_USERNAME);
        assertNotNull(token);

        String username = jwtService.getUsernameByToken(token);
        assertEquals(AuthTestDataSource.DELETED_USERNAME, username);
    }
}
