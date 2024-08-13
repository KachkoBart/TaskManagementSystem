package org.example.taskmanagementsystem.Api.Model.DTO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<TaskDTO> authoredTasks;
    private List<TaskDTO> executorTasks;
}
