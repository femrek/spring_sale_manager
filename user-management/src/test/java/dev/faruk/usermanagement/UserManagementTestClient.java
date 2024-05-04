package dev.faruk.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.auth.service.UserManagementService;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import feign.Client;
import feign.Request;
import feign.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UserManagementTestClient implements Client {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final UserManagementService userManagementService;

    public UserManagementTestClient(UserRepository userRepository,
                                    ObjectMapper objectMapper,
                                    UserManagementService userManagementService) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.userManagementService = userManagementService;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        final String path = request.url().substring(request.url().indexOf("/", 8));
        switch (request.httpMethod()) {
            case POST -> {
                if (path.equals("/api/v1/auth/user-management/") || path.equals("/api/v1/auth/user-management")) {
                    final UserCreateRequest userCreateRequest = objectMapper.readValue(request.body(), UserCreateRequest.class);
                    final UserDTO user = userManagementService.createUser(userCreateRequest);
                    final String body = objectMapper.writeValueAsString(user);
                    return Response.builder()
                            .status(200)
                            .body(body, StandardCharsets.UTF_8)
                            .request(request)
                            .build();
                }
                throw new IOException("Path not supported");
            }
            case PATCH -> {
                if (path.startsWith("/api/v1/auth/user-management/")) {
                    final Long userId = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                    final UserUpdateRequest userUpdateRequest = objectMapper.readValue(request.body(), UserUpdateRequest.class);
                    final UserDTO user = userManagementService.updateUser(userId, userUpdateRequest);
                    final String body = objectMapper.writeValueAsString(user);
                    return Response.builder()
                            .status(200)
                            .body(body, StandardCharsets.UTF_8)
                            .request(request)
                            .build();
                }
                throw new IOException("Path not supported");
            }
            case DELETE -> {
                if (path.startsWith("/api/v1/auth/user-management/")) {
                    final Long userId = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                    userManagementService.deleteUser(userId);
                    return Response.builder()
                            .status(200)
                            .request(request)
                            .build();
                }
                throw new IOException("Path not supported");
            }
            default -> throw new IOException("Method not supported");
        }
    }
}
