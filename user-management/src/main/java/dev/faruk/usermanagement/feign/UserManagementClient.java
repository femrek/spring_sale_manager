package dev.faruk.usermanagement.feign;

import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * This is a Feign client for performing crud operations on users. In auth module, there is a controller called
 * UserManagementController for handling the requests for CRUD operations on users. This client is used to perform crud
 * operations on users by send the requests to that controller.
 */
@FeignClient(name = "user-management-service")
public interface UserManagementClient {
    @RequestLine(value = "POST /auth/user-management/")
    @Headers({"Accept: application/json", "Content-Type: application/json", "Authorization: {authHeader}"})
    @Body("userCreateRequest")
    UserDTO createUser(UserCreateRequest userCreateRequest,
                       @Param("authHeader") String token);

    @RequestLine(value = "PATCH /auth/user-management/{userId}")
    @Headers({"Accept: application/json", "Content-Type: application/json", "Authorization: {authHeader}"})
    @Body("userUpdateRequest")
    UserDTO updateUser(UserUpdateRequest userUpdateRequest,
                       @Param("userId") Long userId,
                       @Param("authHeader") String token);

    @RequestLine(value = "DELETE /auth/user-management/{userId}")
    @Headers({"Accept: application/json", "Content-Type: application/json", "Authorization: {authHeader}"})
    void deleteUser(@Param("userId") Long userId,
                    @Param("authHeader") String token);
}
