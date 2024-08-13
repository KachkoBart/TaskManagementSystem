package org.example.taskmanagementsystem.Api.Model.Mapper;

import org.example.taskmanagementsystem.Api.Model.DTO.TaskDTO;
import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
import org.example.taskmanagementsystem.Api.Model.Enum.Priority;
import org.example.taskmanagementsystem.Api.Model.Enum.TaskStatus;
import org.example.taskmanagementsystem.Api.Repository.Entity.Task;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    private final ModelMapper modelMapper;

    public UserConverter() {
        this.modelMapper = new ModelMapper();
        TypeMap<User, UserDTO> typeMap = modelMapper.getTypeMap(User.class, UserDTO.class);
        if(typeMap == null){
            typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        }
        typeMap.setConverter(new UsersListConverter());
        TypeMap<UserDTO, User> typeMap1 = modelMapper.getTypeMap(UserDTO.class, User.class);
        if(typeMap1 == null){
            typeMap1 = modelMapper.createTypeMap(UserDTO.class, User.class);
        }
        typeMap1.setConverter(new UsersDTOListConverter());
    }

    public UserDTO convertToDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }
    public User convertToEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public class UsersListConverter extends AbstractConverter<User, UserDTO> {

        @Override
        protected UserDTO convert(User user) {
            if(user == null)
                return null;
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setPassword(user.getPassword());
            if(user.getAuthorTasks() == null)
                return userDTO;
            userDTO.setAuthoredTasks(user.getAuthorTasks()
                    .stream()
                    .map((a) -> {
                        TaskDTO task = new TaskDTO();
                        task.setId(a.getId());
                        task.setDescription(a.getDescription());
                        task.setStatus(a.getStatus());
                        task.setPriority(a.getPriority());
                        task.setTitle(a.getTitle());
                        task.setComments(a.getComments());
                        return task;
                    })
                    .collect(Collectors.toList()));
            if(user.getExecutorsTasks() == null)
                return userDTO;
            userDTO.setExecutorTasks(user.getExecutorsTasks()
                    .stream()
                    .map((a) -> {
                        TaskDTO task = new TaskDTO();
                        task.setId(a.getId());
                        task.setDescription(a.getDescription());
                        task.setStatus(a.getStatus());
                        task.setPriority(a.getPriority());
                        task.setTitle(a.getTitle());
                        task.setComments(a.getComments());
                        return task;
                    })
                    .collect(Collectors.toList()));
            return userDTO;
        }
    }
    public class UsersDTOListConverter extends AbstractConverter<UserDTO, User> {

        @Override
        protected User convert(UserDTO user) {
            if(user == null)
                return null;
            User userDTO = new User();
            userDTO.setEmail(user.getEmail());
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setPassword(user.getPassword());
            if(user.getAuthoredTasks() == null)
                return userDTO;
            userDTO.setAuthorTasks(user.getAuthoredTasks()
                    .stream()
                    .map((a) -> {
                        Task task = new Task();
                        task.setId(a.getId());
                        task.setDescription(a.getDescription());
                        task.setStatus(a.getStatus());
                        task.setPriority(a.getPriority());
                        task.setTitle(a.getTitle());
                        task.setComments(a.getComments());
                        return task;
                    })
                    .collect(Collectors.toList()));
            if(user.getExecutorTasks() == null)
                return userDTO;
            userDTO.setExecutorsTasks(user.getExecutorTasks()
                    .stream()
                    .map((a) -> {
                        Task task = new Task();
                        task.setId(a.getId());
                        task.setDescription(a.getDescription());
                        task.setStatus(a.getStatus());
                        task.setPriority(a.getPriority());
                        task.setTitle(a.getTitle());
                        task.setComments(a.getComments());
                        return task;
                    })
                    .collect(Collectors.toList()));
            return userDTO;
        }
    }
}
