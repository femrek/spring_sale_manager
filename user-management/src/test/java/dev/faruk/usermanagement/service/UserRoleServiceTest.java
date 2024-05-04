package dev.faruk.usermanagement.service;

import dev.faruk.commoncodebase.dto.UserRoleDTO;
import dev.faruk.usermanagement.UserTestConfigurer;
import dev.faruk.usermanagement.UserTestDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserTestConfigurer.class)
class UserRoleServiceTest {
    private final UserRoleService userRoleService;
    private final UserTestDataSource userTestDataSource;

    @Autowired
    public UserRoleServiceTest(UserRoleService userRoleService, UserTestDataSource userTestDataSource) {
        this.userRoleService = userRoleService;
        this.userTestDataSource = userTestDataSource;
    }

    @BeforeEach
    void setUp() {
        userTestDataSource.cleanDatabase();
        userTestDataSource.loadDatabase();
    }

    @AfterEach
    void tearDown() {
        userTestDataSource.cleanDatabase();
    }

    @Test
    void showRoleById() {
        List<UserRoleDTO> roles =  userRoleService.showRoles();

        for (UserRoleDTO role : roles) {
            assertNotNull(role.getId());
            assertNotNull(role.getName());
        }
    }
}