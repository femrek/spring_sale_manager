package dev.faruk.usermanagement.service;

import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.repository.base.UserRepository;
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
class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserTestDataSource userTestDataSource;

    @Autowired
    public UserServiceTest(UserService userService,
                           UserRepository userRepository,
                           UserTestDataSource userTestDataSource) {
        this.userService = userService;
        this.userRepository = userRepository;
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
    void showUsers() {
        List<UserDTO> users = userService.showUsers();

        assertEquals(7, users.size());
    }

    @Test
    void showUserById() {
        List<AppUser> users = userRepository.findAll();
        AppUser user = users.stream().filter(u -> u.getUsername().equals("user1")).findAny().orElseThrow();

        UserDTO userDTO = userService.showUserById(user.getId());

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getRoles().size(), userDTO.getRoles().size());
        for (int i = 0; i < user.getRoles().size(); i++) {
            assertEquals(user.getRoles().get(i).getId(), userDTO.getRoles().get(i).getId());
            assertEquals(user.getRoles().get(i).getName(), userDTO.getRoles().get(i).getName());
        }
    }

    @Test
    void createUser() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .username("newUser")
                .password("newUserPassword")
                .name("New User")
                .roleIds(List.of(1L, 2L))
                .build();
        userService.createUser(userCreateRequest, "Bearer token");

        AppUser user = userRepository.findByUsername("newUser");
        assertNotNull(user);
        assertEquals("newUser", user.getUsername());
        assertEquals("New User", user.getName());
        assertEquals(2, user.getRoles().size());
    }

    @Test
    void updateUser() {
        List<AppUser> users = userRepository.findAll();
        AppUser user = users.get(0);

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .name("Updated User")
                .roleIds(List.of(1L, 2L))
                .build();

        UserDTO updatedUserDTO = userService.updateUser(user.getId(), userUpdateRequest, "Bearer token");
        AppUser updatedUser = userRepository.findById(user.getId());

        assertEquals(user.getId(), updatedUserDTO.getId());
        assertEquals("Updated User", updatedUserDTO.getName());
        assertEquals(updatedUserDTO.getName(), updatedUser.getName());
        assertEquals(2, updatedUserDTO.getRoles().size());
        assertEquals(2, updatedUser.getRoles().size());

    }

    @Test
    void deleteUser() {
        List<AppUser> users = userRepository.findAll();
        AppUser user = users.get(0);

        userService.deleteUser(user.getId(), "Bearer token");
        AppUser deletedUser = userRepository.findById(user.getId());

        assertNotNull(deletedUser);
        assertTrue(deletedUser.getDeleted());
    }
}