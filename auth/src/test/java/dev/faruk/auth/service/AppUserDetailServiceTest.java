package dev.faruk.auth.service;

import dev.faruk.auth.AuthTestConfiguration;
import dev.faruk.auth.AuthTestDataSource;
import dev.faruk.auth.dto.AppUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthTestConfiguration.class)
class AppUserDetailServiceTest {
    private final AppUserDetailService appUserDetailService;
    private final AuthTestDataSource authTestDataSource;

    @Autowired
    public AppUserDetailServiceTest(AppUserDetailService appUserDetailService,
                                    AuthTestDataSource authTestDataSource) {
        this.appUserDetailService = appUserDetailService;
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
    void loadUserByUsername_adminUser() {
        AppUserDetails adminUserDetails = appUserDetailService.loadUserByUsername(AuthTestDataSource.ADMIN_USERNAME);

        assertNotNull(adminUserDetails);
        assertEquals(AuthTestDataSource.ADMIN_USERNAME, adminUserDetails.getUsername());
        assertTrue(adminUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        assertFalse(adminUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CASHIER")));
        assertFalse(adminUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("MANAGER")));
        assertTrue(adminUserDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_cashierUser() {
        // create AppUserDetails objects
        AppUserDetails cashierUserDetails = appUserDetailService.loadUserByUsername(AuthTestDataSource.CASHIER_USERNAME);

        assertNotNull(cashierUserDetails);
        assertEquals(AuthTestDataSource.CASHIER_USERNAME, cashierUserDetails.getUsername());
        assertFalse(cashierUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        assertTrue(cashierUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CASHIER")));
        assertFalse(cashierUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("MANAGER")));
        assertTrue(cashierUserDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_managerUser() {
        AppUserDetails managerUserDetails = appUserDetailService.loadUserByUsername(AuthTestDataSource.MANAGER_USERNAME);

        assertNotNull(managerUserDetails);
        assertEquals(AuthTestDataSource.MANAGER_USERNAME, managerUserDetails.getUsername());
        assertFalse(managerUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        assertFalse(managerUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CASHIER")));
        assertTrue(managerUserDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("MANAGER")));
        assertTrue(managerUserDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_deletedUser() {
        AppUserDetails deletedUserDetails = appUserDetailService.loadUserByUsername(AuthTestDataSource.DELETED_USERNAME);

        assertNotNull(deletedUserDetails);
        assertEquals(AuthTestDataSource.DELETED_USERNAME, deletedUserDetails.getUsername());
        assertFalse(deletedUserDetails.isEnabled());
    }
}
