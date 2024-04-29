package dev.faruk.auth.service;

import dev.faruk.auth.AuthTestConfiguration;
import dev.faruk.auth.AuthTestDataSource;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthTestConfiguration.class)
class UserManagementServiceTest {
    private final UserManagementService userManagementService;
    private final UserRepository userRepository;
    private final AuthTestDataSource authTestDataSource;

    @Autowired
    public UserManagementServiceTest(UserManagementService userManagementService,
                                     UserRepository userRepository,
                                     AuthTestDataSource authTestDataSource) {
        this.userManagementService = userManagementService;
        this.userRepository = userRepository;
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
    void createUser_existingUser() {
        // try to create a new user with an existing username
        UserCreateRequest existingUserRequest = UserCreateRequest.builder()
                .username(AuthTestDataSource.ADMIN_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .name("Test User Admin")
                .roleIds(List.of(authTestDataSource.getAdminRole().getId()))
                .build();

        assertThrows(AppHttpError.BadRequest.class, () ->
                userManagementService.createUser(existingUserRequest));
    }

    @Test
    void createUser_deletedUser() {
        // try to create a new user with an existing deleted user username
        UserCreateRequest deletedUserRequest = UserCreateRequest.builder()
                .username(AuthTestDataSource.DELETED_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .name("Test User Deleted")
                .roleIds(List.of(authTestDataSource.getAdminRole().getId()))
                .build();

        assertThrows(AppHttpError.BadRequest.class, () ->
                userManagementService.createUser(deletedUserRequest));
    }

    @Test
    void createUser_newUser() {
        final String NEW_USERNAME = "newUser";

        // create a new user
        UserCreateRequest newUserRequest = UserCreateRequest.builder()
                .username(NEW_USERNAME)
                .password(AuthTestDataSource.PASSWORD)
                .name("New User")
                .roleIds(List.of(
                        authTestDataSource.getAdminRole().getId(),
                        authTestDataSource.getCashierRole().getId()
                ))
                .build();

        UserDTO createdUser = userManagementService.createUser(newUserRequest);

        // check if the user is created
        assertNotNull(createdUser);

        // check if the new user is created
        assertNotNull(userRepository.findByUsername(NEW_USERNAME));

        // check if the password is encoded
        assertNotEquals(AuthTestDataSource.PASSWORD, userRepository.findByUsername(NEW_USERNAME).getPassword());

        // check if the roles are added
        assertEquals(2, userRepository.findByUsername(NEW_USERNAME).getRoles().size());
        assertNotNull(userRepository.findByUsername(NEW_USERNAME).getRoles().stream()
                .filter(role -> role.getName().equals("ADMIN")).findFirst().orElse(null));
        assertNotNull(userRepository.findByUsername(NEW_USERNAME).getRoles().stream()
                .filter(role -> role.getName().equals("CASHIER")).findFirst().orElse(null));

        // check if the user is not deleted
        assertFalse(userRepository.findByUsername(NEW_USERNAME).getDeleted());
    }

    @Test
    void updateUser_existingUser() {
        UserDTO updatedUser = userManagementService.updateUser(
                authTestDataSource.getAdminUser().getId(),
                UserUpdateRequest.builder()
                        .password("updatedPassword")
                        .name("Updated User Admin")
                        .roleIds(List.of(authTestDataSource.getCashierRole().getId()))
                        .build());


        assertEquals("Updated User Admin", updatedUser.getName());
    }

    @Test
    void updateUser_doesNotExistUser() {
        final Long userId = authTestDataSource.getAdminUser().getId();
        userRepository.deleteByUsername(authTestDataSource.getAdminUser().getUsername());

        assertThrows(AppHttpError.NotFound.class, () ->
                userManagementService.updateUser(userId, UserUpdateRequest.builder()
                        .password("updatedPassword")
                        .name("Updated User Admin")
                        .roleIds(List.of(authTestDataSource.getCashierRole().getId()))
                        .build()));
    }

    @Test
    void updateUser_deletedUser() {
        assertThrows(AppHttpError.NotFound.class, () ->
                userManagementService.updateUser(
                        authTestDataSource.getDeletedUser().getId(),
                        UserUpdateRequest.builder()
                                .password("updatedPassword")
                                .name("Updated User Deleted")
                                .roleIds(List.of(authTestDataSource.getCashierRole().getId()))
                                .build())
        );
    }

    @Test
    void deleteUser() {
    }
}
