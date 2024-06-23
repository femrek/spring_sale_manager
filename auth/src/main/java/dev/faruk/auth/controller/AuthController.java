package dev.faruk.auth.controller;

import dev.faruk.auth.dto.LoginResponse;
import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.auth.dto.LoginRequest;
import dev.faruk.commoncodebase.dto.UserDTO;
import dev.faruk.auth.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/login")
    public ResponseEntity<AppSuccessResponse<LoginResponse>> login(@RequestBody LoginRequest userCredentials) {
        LoginResponse response = authService.generateToken(userCredentials);
        return new AppSuccessResponse<>("token is provided", response).toResponseEntity();
    }

    @GetMapping("/user")
    public ResponseEntity<AppSuccessResponse<UserDTO>> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        final UserDTO user = authService.getUserByToken(authHeader);
        return new AppSuccessResponse<>("user found by authentication token successfully", user).toResponseEntity();
    }
}
