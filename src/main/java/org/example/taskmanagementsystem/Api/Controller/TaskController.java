package org.example.taskmanagementsystem.Api.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskmanagementsystem.Api.Model.DTO.TaskDTO;
import org.example.taskmanagementsystem.Api.Model.Enum.TaskStatus;
import org.example.taskmanagementsystem.Api.Service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    @PostMapping
    @Operation(
            summary = "Создание задачи",
            description = "Добавление задачи, автором будет текущий пользователь"
    )
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto) {
        log.info("Creating task: {}", taskDto);
        return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
    }
    @GetMapping
    @Operation(
            summary = "Получение всех задач всех пользователей"
    )
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("Getting all tasks");
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }
    @GetMapping("/{taskId}")
    @Operation(
            summary = "Получение задачи по ее ID",
            description = "Поиск задачи по id в базе данных"
    )
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        log.info("Getting task by id: {}", taskId);
        return new ResponseEntity<>(taskService.getTaskById(taskId), HttpStatus.OK);
    }
    @PatchMapping("/update")
    @Operation(
            summary = "Обновление задачи по ее ID",
            description = "Изменять задачу может только ее автор.Обновить можно: название, описание, приоритет и статус выполенения"
    )
    public ResponseEntity<TaskDTO> updateTask(@RequestBody @Valid TaskDTO updatedTaskDto) {
        log.info("Updating task: {}", updatedTaskDto);
        return new ResponseEntity<>(taskService.updateTask(updatedTaskDto), HttpStatus.OK);
    }
    @PostMapping("/{taskId}/comments/{email}")
    @Operation(
            summary = "Добавление комментария к задаче",
            description = "Добавление комментария к задаче по ее ID и email автора комментария"
    )
    public ResponseEntity<TaskDTO> addComment(@PathVariable Long taskId, @PathVariable String email, @RequestBody String comment) {
        log.info("Adding comment: {}", comment);
        return new ResponseEntity<>(taskService.addComment(comment, taskId, email), HttpStatus.OK);
    }
    @PatchMapping("/{taskId}/status")
    @Operation(
            summary = "Обновление статуса задачи по ее ID",
            description = "Изменять статус задачи может только ее автор"
    )
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long taskId,
                                                 @RequestBody @Valid TaskStatus newStatus) {
        log.info("Updating task with ID: {} status: {}", taskId, newStatus);
        return new ResponseEntity<>(taskService.updateStatus(taskId, newStatus), HttpStatus.OK);
    }
    @PostMapping("/{taskId}/addExecutor/{executorId}")
    @Operation(
            summary = "Добавление исполнителя задачи",
            description = "Добавлять может только автор задачи"
    )
    public ResponseEntity<TaskDTO> addTaskExecutor(@PathVariable Long taskId, @PathVariable Long executorId) {
        log.info("Adding executor: {} in task with id: {}", executorId, taskId);
        return new ResponseEntity<>(taskService.addExecutors(taskId, executorId), HttpStatus.OK);
    }
    @PostMapping("/{taskId}/removeExecutor/{executorId}")
    @Operation(
            summary = "Удаление исполнителя задачи",
            description = "Удалять может только автор задачи"
    )
    public ResponseEntity<TaskDTO> removeTaskExecutor(@PathVariable Long taskId, @PathVariable Long executorId) {
        log.info("Removing executor: {} in task with id: {}", executorId, taskId);
        return new ResponseEntity<>(taskService.removeExecutors(taskId, executorId), HttpStatus.OK);
    }
    @DeleteMapping("/{taskId}")
    @Operation(
            summary = "Удаление задачи",
            description = "Удалять может только автор задачи"
    )
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        log.info("Deleting task: {}", taskId);
        taskService.deleteTaskById(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
