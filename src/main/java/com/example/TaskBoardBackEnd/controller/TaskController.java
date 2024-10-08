package com.example.TaskBoardBackEnd.controller;

import com.example.TaskBoardBackEnd.dto.TaskReqResDto;
import com.example.TaskBoardBackEnd.model.ETaskStatus;
import com.example.TaskBoardBackEnd.model.Task;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.TaskRepository;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private TaskReqResDto convertToDto(Task task) {
        return TaskReqResDto.builder()
                .idTask(task.getIdTask())
                .user(task.getUser())
                .title(task.getTitle())
                .description(task.getDescription())
                .ETaskStatus(task.getETaskStatus())
                .createdAt(task.getCreatedAt())
                .UpdatedAt(task.getUpdatedAt())
                .build();
    }


    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<TaskReqResDto>> getAllTask(@PathVariable Long id) {
        List<Task> task = taskRepository.findTasksByUserId(id);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<TaskReqResDto> taskDto = task.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/task/{id}")
    public ResponseEntity<TaskReqResDto> createTask(@RequestBody Task task, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (task.getUser() == null || task.getTitle().isEmpty() || task.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Task newTask = Task.builder()
                .user(user)
                .title(task.getTitle())
                .description(task.getDescription())
                .ETaskStatus(ETaskStatus.InProgress)
                .createdAt(LocalDate.now())
                .UpdatedAt(LocalDate.now())
                .build();
        Task savedTask = taskRepository.save(newTask);
        TaskReqResDto taskReqResDto = convertToDto(savedTask);
        return ResponseEntity.ok(taskReqResDto);
    }

}