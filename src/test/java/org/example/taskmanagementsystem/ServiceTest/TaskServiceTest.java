package org.example.taskmanagementsystem.ServiceTest;

import org.example.taskmanagementsystem.Api.Model.DTO.TaskDTO;
import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
import org.example.taskmanagementsystem.Api.Model.Enum.Priority;
import org.example.taskmanagementsystem.Api.Model.Enum.TaskStatus;
import org.example.taskmanagementsystem.Api.Repository.Entity.Task;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Repository.TaskRepository;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.example.taskmanagementsystem.Api.Service.TaskService;
import org.example.taskmanagementsystem.Api.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class TaskServiceTest {

    @Autowired
    private TaskService service;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createTest(){
        Task task = getTask();
        TaskDTO taskDTO = getTaskDTO();

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        assertEquals(taskDTO.getId(), service.createTask(taskDTO).getId());
    }
    @Mock
    private Authentication auth;
    @Test
    public void addExecutorTest(){
        User user = getUser();
        Task task = getTask();
        task.setAuthor(user);
        user.setExecutorsTasks(new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("aaa@mail.ru");
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        assertEquals(user.getEmail(), service.addExecutors(task.getId(), user.getId()).getExecutors().get(0).getEmail());
    }
    private Task getTask(){
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setTitle("Task Title");
        task.setAuthor(getUser());
        task.setComments(new ArrayList<>());
        task.setExecutors(new ArrayList<>());
        return task;
    }
    private TaskDTO getTaskDTO(){
        TaskDTO task = new TaskDTO();
        task.setId(1L);
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setTitle("Task Title");
        task.setAuthor(getUserDTO());
        task.setComments(new ArrayList<>());
        task.setExecutors(new ArrayList<>());
        return task;
    }
    private UserDTO getUserDTO(){
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setEmail("aaa@mail.ru");
        user.setPassword("password");
        return user;
    }
    private User getUser(){
        User user = new User();
        user.setId(1L);
        user.setEmail("aaa@mail.ru");
        user.setPassword("password");
        return user;
    }
}

