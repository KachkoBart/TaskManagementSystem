package org.example.taskmanagementsystem.Api.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Создание пользователя"
    )
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto) {
        log.info("Creating user with email: {}", userDto.getEmail());
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }
    @GetMapping
    @Operation(
            summary = "Список всех пользователей"
    )
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Getting all users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/{email}")
    @Operation(
            summary = "Получение пользователя по email"
    )
    public ResponseEntity<?> getUserByUsername(@PathVariable String email) {
        log.info("Getting user by email: {}", email);
        return new ResponseEntity<>(userService.getUserByUsername(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Изменение информации пользователя",
            description = "Можно изменить имя и пароль"
    )
    @PatchMapping("/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String email, @RequestBody UserDTO userDTO) {
        log.info("Updating user with email: {}, User: {}", email, userDTO);
        return new ResponseEntity<>(userService.updateUser(email, userDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{email}")
    @Operation(
            summary = "Удаление информации о пользовате"
    )
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        log.info("Deleting user with email: {}", email);
        userService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
