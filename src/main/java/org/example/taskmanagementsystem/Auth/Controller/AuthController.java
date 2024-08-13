package org.example.taskmanagementsystem.Auth.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.example.taskmanagementsystem.Auth.DTO.AuthRequest;
import org.example.taskmanagementsystem.Auth.DTO.AuthResponse;
import org.example.taskmanagementsystem.Auth.DTO.RegisterRequest;
import org.example.taskmanagementsystem.Auth.Service.AuthService;
import org.example.taskmanagementsystem.Exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "JWT-security", description = "Methods for registration and authentication")
@Slf4j
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping(value = "/sign-in", produces="application/json")
    @Operation(
            summary = "Авторизация пользователя"
    )
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authenticationRequest) {
        log.info("login request: {}", authenticationRequest);
        if (!authenticationService.isCredentialsValid(authenticationRequest)){
            throw new AuthException("Invalid credentials");
        }

        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping(value = "/sign-up", produces="application/json")
    @Operation(
            summary = "Регистрация пользователя"
    )
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest registerRequest) {
        log.info("register request: {}", registerRequest);
        if (authenticationService.userExists(registerRequest.getUsername())) {
            throw new AuthException("username already exists");
        }

        AuthResponse response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}