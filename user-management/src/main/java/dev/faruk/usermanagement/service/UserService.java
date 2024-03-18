package dev.faruk.usermanagement.service;

import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.UserRepository;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.usermanagement.feign.FeignExceptionMapper;
import dev.faruk.usermanagement.feign.UserManagementClient;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService is the class that handles the business logic for the users. mostly CRUD operations.
 */
@Service
public class UserService {
    private static final String HOST = "http://localhost:8080";
    private final UserRepository userRepository;
    private final FeignExceptionMapper feignExceptionMapper;

    @Autowired
    public UserService(UserRepository userRepository, FeignExceptionMapper feignExceptionMapper) {
        this.userRepository = userRepository;
        this.feignExceptionMapper = feignExceptionMapper;
    }

    /**
     * Returns all the users exist in the database. Only the users that are not deleted.
     *
     * @return List of UserDTO converted from AppUser entity set from database.
     */
    public List<UserDTO> showUsers() {
        return userRepository.findAllOnlyExist().stream().map(UserDTO::new).toList();
    }

    /**
     * Returns the user with the given id. if the user is deleted, it will not be returned.
     *
     * @param id id of the user
     * @return UserDTO object converted from AppUser entity from database.
     */
    public UserDTO showUserById(Long id) {
        final AppUser user = userRepository.findOnlyExistById(id);
        if (user == null) throw new AppHttpError.NotFound("User not found with id " + id);
        return new UserDTO(user);
    }

    /**
     * Creates a new user with the given userCreateRequest. Sends post request to auth service.
     *
     * @param userCreateRequest user creation request
     * @param authHeader        authorization header starts with "Bearer"
     * @return UserDTO of created user if success.
     */
    public UserDTO createUser(UserCreateRequest userCreateRequest, String authHeader) {
        UserManagementClient userManagementClient = Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserManagementClient.class, HOST);
        try {
            final UserDTO response = userManagementClient.createUser(userCreateRequest, authHeader);
            if (response != null) return response;
        } catch (FeignException e) {
            throw feignExceptionMapper.map(e);
        }

        throw new AppHttpError.InternalServerError("User creation failed");
    }

    /**
     * Updates the user with the given userUpdateRequest. Sends patch request to auth service.
     *
     * @param userId             id of the user to update
     * @param userUpdateRequest  user update request
     * @param authHeader         authorization header starts with "Bearer"
     * @return UserDTO of updated user if success.
     */
    public UserDTO updateUser(Long userId, UserUpdateRequest userUpdateRequest, String authHeader) {
        if (userUpdateRequest == null) throw new AppHttpError.BadRequest("User update request is required");
        if (userUpdateRequest.isEmpty()) throw new AppHttpError.BadRequest("There is nothing provided to update user");
        UserManagementClient userManagementClient = Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserManagementClient.class, HOST);
        try {
            final UserDTO response = userManagementClient.updateUser(userUpdateRequest, userId, authHeader);
            if (response != null) return response;
        } catch (FeignException e) {
            throw feignExceptionMapper.map(e);
        }

        throw new AppHttpError.InternalServerError("User update failed");
    }

    /**
     * Deletes the user with the given id. Sends delete request to auth service.
     *
     * @param userId     id of the user to delete
     * @param authHeader authorization header starts with "Bearer"
     */
    public void deleteUser(Long userId, String authHeader) {
        UserManagementClient userManagementClient = Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserManagementClient.class, HOST);
        try {
            userManagementClient.deleteUser(userId, authHeader);
        } catch (FeignException e) {
            throw feignExceptionMapper.map(e);
        }
    }
}
