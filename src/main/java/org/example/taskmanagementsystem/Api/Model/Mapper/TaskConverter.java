package org.example.taskmanagementsystem.Api.Model.Mapper;

import ch.qos.logback.classic.pattern.DateConverter;
import org.example.taskmanagementsystem.Api.Model.DTO.TaskDTO;
import org.example.taskmanagementsystem.Api.Model.DTO.UserDTO;
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
public class TaskConverter {
    private final ModelMapper modelMapper;

    public TaskConverter() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TypeMap<Task, TaskDTO> typeMap = modelMapper.getTypeMap(Task.class, TaskDTO.class);
        if(typeMap == null){
            typeMap = modelMapper.createTypeMap(Task.class, TaskDTO.class);
        }
        typeMap.setConverter(new TasksListConverter());
        TypeMap<TaskDTO, Task> typeMap1 = modelMapper.getTypeMap(TaskDTO.class, Task.class);
        if(typeMap1 == null){
            typeMap1 = modelMapper.createTypeMap(TaskDTO.class, Task.class);
        }
        typeMap1.setConverter(new TasksDTOListConverter());
    }

    public TaskDTO convertToDto(Task entity) {
        return modelMapper.map(entity, TaskDTO.class);
    }

    public Task convertToEntity(TaskDTO dto) {
        return modelMapper.map(dto, Task.class);
    }

    public static class TasksListConverter extends AbstractConverter<Task, TaskDTO> {

        @Override
        protected TaskDTO convert(Task task) {
            if (task == null)
                return null;
            TaskDTO dto = new TaskDTO();
            dto.setId(task.getId());
            dto.setDescription(task.getDescription());
            dto.setTitle(task.getTitle());
            dto.setStatus(task.getStatus());
            dto.setPriority(task.getPriority());
            dto.setCreatedDate(task.getCreatedAt());

            UserDTO userDTO = new UserDTO();
            User user = task.getAuthor();
            userDTO.setEmail(user.getEmail());
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());

            dto.setAuthor(userDTO);
            if(task.getExecutors() == null){
                return dto;
            }
            dto.setExecutors(task.getExecutors().stream()
                    .map((a) -> {
                        UserDTO userDTOl = new UserDTO();
                        userDTOl.setEmail(a.getEmail());
                        userDTOl.setId(a.getId());
                        userDTOl.setName(a.getName());
                        return userDTOl;
                    }).collect(Collectors.toList()));
            return dto;
        }
    }
    public static class TasksDTOListConverter extends AbstractConverter<TaskDTO, Task> {

        @Override
        protected Task convert(TaskDTO taskDTO) {
            if (taskDTO == null)
                return null;
            Task task = new Task();
            task.setId(taskDTO.getId());
            task.setDescription(taskDTO.getDescription());
            task.setTitle(taskDTO.getTitle());
            task.setStatus(taskDTO.getStatus());
            task.setPriority(taskDTO.getPriority());

            UserDTO userDTO = taskDTO.getAuthor();
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());

            task.setAuthor(user);
            if(taskDTO.getExecutors() == null){
                return task;
            }
            task.setExecutors(taskDTO.getExecutors().stream()
                    .map((a) -> {
                        User userl = new User();
                        userl.setEmail(a.getEmail());
                        userl.setId(a.getId());
                        userl.setName(a.getName());
                        return userl;
                    }).collect(Collectors.toList()));
            return task;
        }
    }
}
