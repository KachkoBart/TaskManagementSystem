package org.example.taskmanagementsystem.ServiceTest;

import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.example.taskmanagementsystem.Api.Service.UserService;
import org.example.taskmanagementsystem.Exception.IllegalActionsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createTestNotExistingUser() throws Exception {
        UserDTO expectedUserDTO = getUserDTO();
        User expectedUser = getUser();

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        UserDTO resultUser = service.createUser(expectedUserDTO);

        assertEquals(resultUser.getEmail(), expectedUserDTO.getEmail());
        assertEquals(resultUser.getPassword(), expectedUserDTO.getPassword());
    }
    @Test
    public void createTestExistingUser() throws Exception {
        UserDTO expectedUserDTO = getUserDTO();
        User expectedUser = getUser();

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userRepository.existsByEmail(expectedUserDTO.getEmail())).thenReturn(true);

        assertThrows(IllegalActionsException.class, () -> service.createUser(expectedUserDTO));
    }
    @Test
    public void updateUserTest() throws Exception {
        UserDTO expectedUserDTO = getUserDTO();
        User expectedUser = getUser();

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userRepository.findByEmail(expectedUserDTO.getEmail())).thenReturn(Optional.of(expectedUser));

        UserDTO resultUser = service.updateUser(expectedUserDTO.getEmail(), expectedUserDTO);
        assertEquals(resultUser.getEmail(), expectedUserDTO.getEmail());
        assertEquals(resultUser.getPassword(), expectedUserDTO.getPassword());
    }

    private UserDTO getUserDTO(){
        UserDTO user = new UserDTO();
        user.setEmail("aaa@mail.ru");
        user.setPassword("password");
        return user;
    }
    private User getUser(){
        User user = new User();
        user.setEmail("aaa@mail.ru");
        user.setPassword("password");
        return user;
    }
}
