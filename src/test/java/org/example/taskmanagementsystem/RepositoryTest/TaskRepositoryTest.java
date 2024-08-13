package org.example.taskmanagementsystem.RepositoryTest;

import org.example.taskmanagementsystem.Api.Model.Enum.Priority;
import org.example.taskmanagementsystem.Api.Model.Enum.TaskStatus;
import org.example.taskmanagementsystem.Api.Repository.Entity.Task;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Repository.TaskRepository;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

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
    private User getUser(){
        User user = new User();
        user.setEmail("aaa@mail.ru");
        user.setPassword("password");
        return user;
    }
    @Test
    public void findById(){
        Task task = getTask();
        task.setAuthor(userRepository.save(getUser()));
        Task result = taskRepository.save(task);
        assertEquals(task.getTitle(), result.getTitle());

        Optional<Task> resultOption = taskRepository.findById(result.getId());
        assertThat(resultOption.isPresent()).isTrue();
        assertEquals(task.getTitle(), resultOption.get().getTitle());
    }
}
