package dev.faruk.auth.controller;

import dev.faruk.auth.service.UserManagementService;
import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.dbLogging.IgnoreDbLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Unlike the other controllers, this controller is only an interface for feign client. Receives request from
 * user-management-service.
 */
@Log4j2
@RestController
@RequestMapping("/user-management")
public class UserManagementController {
    private final UserManagementService userManagementService;

    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @IgnoreDbLog
    @PostMapping({"/", ""})
    public UserDTO createUser(@RequestBody UserCreateRequest userCreateRequest) {
        return userManagementService.createUser(userCreateRequest);
    }

    @IgnoreDbLog
    @PatchMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return userManagementService.updateUser(id, userUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
    }
}
