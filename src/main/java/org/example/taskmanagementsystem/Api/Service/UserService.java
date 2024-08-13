package org.example.taskmanagementsystem.Api.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskmanagementsystem.Api.Model.DTO.TaskDTO;
import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
import org.example.taskmanagementsystem.Api.Model.Mapper.TaskConverter;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Model.Mapper.UserConverter;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.example.taskmanagementsystem.Exception.IllegalActionsException;
import org.example.taskmanagementsystem.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final TaskConverter taskConverter;
    public List<UserDTO> getAllUsers() {
        log.debug("Get all users");
        return userRepository.findAll().stream().map(userConverter::convertToDto).toList();
    }

    public UserDTO getUserById(Long userId) {
        log.debug("Get user by id: {}", userId);
        return userRepository.findById(userId).map(userConverter::convertToDto).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.error("User with email: {} already exists", userDTO.getEmail());
            throw new IllegalActionsException("User with this email already exists");
        }
        return userConverter.convertToDto(userRepository.save(userConverter.convertToEntity(userDTO)));
    }
    @Transactional
    public UserDTO updateUser(String email, UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if(userDTO.getName() != null){
            log.debug("Updating name: {} for user: {}", userDTO.getName(), userDTO);
            existingUser.setName(userDTO.getName());
        }
        if(userDTO.getPassword() != null){
            log.debug("Updating password: {} for user: {}", userDTO.getPassword(), userDTO);
            existingUser.setPassword(userDTO.getPassword());
        }
        return userConverter.convertToDto(userRepository.save(existingUser));
    }

    public UserDTO getUserByUsername(String email) {
        log.debug("Get user by email: {}", email);
        return userRepository.findByEmail(email).map(userConverter::convertToDto).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public void addTaskToUser(Long userId, TaskDTO task) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        if(user.getAuthorTasks() == null){
            user.setAuthorTasks(new ArrayList<>());
        }
        user.getAuthorTasks().add(taskConverter.convertToEntity(task));
        log.debug("Set author task: {} for user with id: {}", task, userId);
        userRepository.save(user);
    }

    public void deleteUser(String email) {
        log.debug("Delete user by email: {}", email);
        userRepository.deleteByEmail(email);
    }
}
