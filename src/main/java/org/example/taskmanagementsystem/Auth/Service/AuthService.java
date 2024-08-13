package org.example.taskmanagementsystem.Auth.Service;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskmanagementsystem.Api.Repository.Entity.Role;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.example.taskmanagementsystem.Auth.DTO.AuthRequest;
import org.example.taskmanagementsystem.Auth.DTO.AuthResponse;
import org.example.taskmanagementsystem.Auth.DTO.RegisterRequest;
import org.example.taskmanagementsystem.Exception.AuthException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        log.debug("Registered save user: {}", user);
        var jwtToken = jwtService.generateToken(user);
        log.debug("Generated JWT for user with email: {}", user.getEmail());
        return AuthResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new AuthException("User not found"));
        log.debug("Authenticated user: {} found in base", user);
        var jwtToken = jwtService.generateToken(user);
        log.debug("Generated JWT for user with email: {}", user.getEmail());
        return AuthResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isCredentialsValid(AuthRequest authenticationRequest) {
        String reqEmail = authenticationRequest.getEmail();
        String reqPassword = authenticationRequest.getPassword();

        String dbPassword = userRepository.findByEmail(reqEmail)
                .orElseThrow(() -> new AuthException("Invalid credentials"))
                .getPassword();

        return passwordEncoder.matches(reqPassword, dbPassword);
    }
}
