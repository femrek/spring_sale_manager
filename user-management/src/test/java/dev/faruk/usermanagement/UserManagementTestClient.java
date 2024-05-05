package dev.faruk.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.commoncodebase.dto.auth.UserCreateRequest;
import dev.faruk.commoncodebase.dto.auth.UserUpdateRequest;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserManagementTestClient implements Client {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public UserManagementTestClient(UserRepository userRepository,
                                    ObjectMapper objectMapper,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        final String path = request.url().substring(request.url().indexOf("/", 8));
        switch (request.httpMethod()) {
            case POST -> {
                if (path.equals("/api/v1/auth/user-management/") || path.equals("/api/v1/auth/user-management")) {
                    final UserCreateRequest userCreateRequest = objectMapper.readValue(request.body(), UserCreateRequest.class);
                    List<AppUserRole> userRoles = new ArrayList<>();
                    for (Long roleId : userCreateRequest.getRoleIds()) {
                        userRoles.add(userRepository.findRoleById(roleId));
                    }
                    final UserDTO user = new UserDTO(userRepository.create(AppUser.builder()
                            .username(userCreateRequest.getUsername())
                            .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                            .name(userCreateRequest.getName())
                            .roles(userRoles)
                            .build()));
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
                    final Long userId = _extractId(path);
                    final UserUpdateRequest userUpdateRequest = objectMapper.readValue(request.body(), UserUpdateRequest.class);
                    final AppUser userToUpdate = userRepository.findById(userId);
                    if (userUpdateRequest.getName() != null) {
                        userToUpdate.setName(userUpdateRequest.getName());
                    }
                    if (userUpdateRequest.getPassword() != null) {
                        userToUpdate.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
                    }
                    List<AppUserRole> userRoles = new ArrayList<>();
                    for (Long roleId : userUpdateRequest.getRoleIds()) {
                        userRoles.add(userRepository.findRoleById(roleId));
                    }
                    userToUpdate.setRoles(userRoles);
                    final UserDTO user = new UserDTO(userRepository.update(userToUpdate));
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
                    final Long userId = _extractId(path);
                    AppUser user = userRepository.findById(userId);
                    userRepository.deleteSoft(user);
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

    private Long _extractId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
