package org.example.taskmanagementsystem.Api.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.taskmanagementsystem.Api.Model.Enum.Priority;
import org.example.taskmanagementsystem.Api.Model.Enum.TaskStatus;
import org.example.taskmanagementsystem.Api.Repository.Entity.Comment;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private UserDTO author;
    private List<UserDTO> executors;
    private List<Comment> comments;
    private Date createdDate;
}
