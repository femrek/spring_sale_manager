package dev.faruk.auth.controller;

import dev.faruk.auth.dto.response.AppSuccessResponse;
import dev.faruk.auth.dto.request.LoginRequest;
import dev.faruk.auth.dto.request.RegisterRequest;
import dev.faruk.auth.dto.response.UserDTO;
import dev.faruk.auth.entity.AppUser;
import dev.faruk.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/register")
    public AppSuccessResponse<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
        AppUser createdUser = authService.saveUser(registerRequest);
        final UserDTO response = new UserDTO(createdUser);
        return new AppSuccessResponse<>(
                HttpStatus.CREATED,
                "user is created, and created user is provided",
                response);
    }

    @PostMapping("/login")
    public AppSuccessResponse<String> login(@RequestBody LoginRequest userCredentials) {
        authService.doesCredentialsValid(userCredentials);
        String token = authService.generateToken(userCredentials.getUsername());
        return new AppSuccessResponse<>("token is provided", token);
    }

    @GetMapping("/user")
    public AppSuccessResponse<UserDTO> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        final UserDTO user = authService.getUserByToken(authHeader);
        return new AppSuccessResponse<>("user found by authentication token successfully", user);
    }
}
