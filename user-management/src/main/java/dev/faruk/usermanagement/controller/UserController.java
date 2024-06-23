package dev.faruk.usermanagement.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.usermanagement.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController is the class that handles the requests for CRUD operations on users.
 */
@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<AppSuccessResponse<List<UserDTO>>> showUsers() {
        List<UserDTO> users = userService.showUsers();
        return new AppSuccessResponse<>("All users are listed successfully", users).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppSuccessResponse<UserDTO>> showUser(@PathVariable Long id) {
        UserDTO user = userService.showUserById(id);
        return new AppSuccessResponse<>("User provided successfully", user).toResponseEntity();
    }

    @PostMapping({"/", ""})
    public ResponseEntity<AppSuccessResponse<UserDTO>> createUser(@RequestBody UserCreateRequest user,
                                                                  @RequestHeader("Authorization") String authHeader) {
        UserDTO createdUser = userService.createUser(user, authHeader);
        return new AppSuccessResponse<>(HttpStatus.CREATED.value(), "User created successfully", createdUser)
                .toResponseEntity();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AppSuccessResponse<UserDTO>> updateUser(@PathVariable Long id,
                                                  @RequestBody UserUpdateRequest userUpdateRequest,
                                                  @RequestHeader("Authorization") String authHeader) {
        UserDTO user = userService.updateUser(id, userUpdateRequest, authHeader);
        return new AppSuccessResponse<>("User updated successfully", user).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppSuccessResponse<Object>> deleteUser(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String authHeader) {
        userService.deleteUser(id, authHeader);
        return new AppSuccessResponse<>("User deleted successfully", null).toResponseEntity();
    }
}
