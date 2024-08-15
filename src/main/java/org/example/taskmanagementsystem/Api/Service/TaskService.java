package org.example.taskmanagementsystem.Api.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskmanagementsystem.Api.Model.DTO.TaskDTO;
import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
import org.example.taskmanagementsystem.Api.Model.Mapper.UserConverter;
import org.example.taskmanagementsystem.Api.Repository.Entity.Comment;
import org.example.taskmanagementsystem.Api.Repository.Entity.Task;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Model.Enum.TaskStatus;
import org.example.taskmanagementsystem.Api.Model.Mapper.TaskConverter;
import org.example.taskmanagementsystem.Api.Repository.TaskRepository;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.example.taskmanagementsystem.Exception.IllegalActionsException;
import org.example.taskmanagementsystem.Exception.TaskNotFoundException;
import org.example.taskmanagementsystem.Exception.UserNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskConverter taskConverter;
    private final UserService service;
    public TaskDTO createTask(TaskDTO task){
        User user = userRepository.findById(task.getAuthor().getId()).orElseThrow(() -> new UserNotFoundException("User with id " + task.getAuthor().getId() + " not found"));
        if(!user.getEmail().equals( task.getAuthor().getEmail()) || !user.getPassword().equals(task.getAuthor().getPassword())){
            log.error("User with id " + task.getAuthor().getId() + " does not have the correct password or email");
            throw new IllegalActionsException("Illegal User arguments User email: " + user.getEmail());
        }
        service.addTaskToUser(task.getAuthor().getId(), task);
        log.debug("Created task with id " + task.getAuthor().getId());
        return taskConverter.convertToDto(taskRepository.save(taskConverter.convertToEntity(task)));
    }
    public List<TaskDTO> getAllTasks(){
        log.debug("Get all tasks");
        return taskRepository.findAll().stream().map(taskConverter::convertToDto).toList();
    }
    public TaskDTO getTaskById(Long id){
        log.debug("Get task with id " + id);
        return taskRepository.findById(id).map(taskConverter::convertToDto).orElseThrow(() -> new TaskNotFoundException("Task by id " + id +" not found"));
    }
    public TaskDTO updateTask(TaskDTO task){
        Task taskEntity = taskRepository.findById(task.getId()).orElseThrow(() -> new TaskNotFoundException("Task by id " + task.getId() +" not found"));
        checkValidUserForChangeTask(task.getAuthor().getId(), taskEntity);
        if(task.getDescription() != null){
            log.debug("Update description " + task.getDescription() + " for task with id " + task.getId());
            taskEntity.setDescription(task.getDescription());
        }
        if(task.getStatus() != null){
            log.debug("Update status " + task.getStatus() + " for task with id " + task.getId());
            taskEntity.setStatus(task.getStatus());
        }
        if(task.getTitle() != null){
            log.debug("Update title " + task.getTitle() + " for task with id " + task.getId());
            taskEntity.setTitle(task.getTitle());
        }
        if(task.getPriority() != null){
            log.debug("Update priority " + task.getPriority() + " for task with id " + task.getId());
            taskEntity.setPriority(task.getPriority());
        }
        return taskConverter.convertToDto(taskRepository.save(taskEntity));
    }
    public TaskDTO addExecutors(Long taskID, Long userID){
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new TaskNotFoundException("Task by id " + taskID +" not found"));
        User user = checkValidUserForChangeTask(task.getAuthor().getId(), task);
        if(user.getExecutorsTasks().stream().anyMatch((a) -> a.getId().equals(taskID))){
            log.error("User with id " + userID + " already has the executors task");
            throw new IllegalActionsException("User with id " + userID + " already in executors task with id " + taskID);
        }
        if(task.getExecutors() == null){
            task.setExecutors(new ArrayList<>());
        }
        if(user.getExecutorsTasks() == null){
            user.setExecutorsTasks(new ArrayList<>());
        }
        User executor = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        executor.getExecutorsTasks().add(task);
        userRepository.save(executor);
        log.debug("Added executors task with id " + taskID);
        task.getExecutors().add(user);
        return taskConverter.convertToDto(taskRepository.save(task));
    }
    public TaskDTO removeExecutors(Long taskID, Long userID){
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new TaskNotFoundException("Task by id " + taskID +" not found"));
        User user = checkValidUserForChangeTask(task.getAuthor().getId(), task);
        if(user.getExecutorsTasks().stream().noneMatch((a) -> a.getId().equals(taskID))){
            log.error("User with id " + userID + " is not the executors task");
            throw new IllegalActionsException("User with id " + userID + " is not executor for task id " + taskID);
        }
        User executor = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        executor.getExecutorsTasks().remove(task);
        userRepository.save(executor);
        task.getExecutors().remove(executor);
        return taskConverter.convertToDto(taskRepository.save(task));
    }
    public TaskDTO addComment(String comment, Long id, String email){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task by id " + id +" not found"));
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        Comment commentEntity = new Comment();
        commentEntity.setComment(comment);
        commentEntity.setEmail(email);
        task.getComments().add(commentEntity);
        log.debug("Added comment " + comment);
        return taskConverter.convertToDto(taskRepository.save(task));
    }
    public TaskDTO updateStatus(Long id, TaskStatus status){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task by id " + id +" not found"));
        task.setStatus(status);
        log.debug("Updated status " + status);
        return taskConverter.convertToDto(taskRepository.save(task));
    }
    public void deleteTaskById(Long id){
        log.debug("Delete task with id " + id);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task by id " + id +" not found"));
        checkValidUserForChangeTask(task.getAuthor().getId(), task);
        taskRepository.deleteById(id);
    }
    private User checkValidUserForChangeTask(Long userID, Task task){
        User user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(task.getAuthor().getEmail())){
            log.error("User with id " + userID + " does not have the email of author");
            throw new IllegalActionsException("User cant remove executor, because he is not author");
        }
        return user;
    }
}
