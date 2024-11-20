package com.example.TaskBoardBackEnd.controller;

import com.example.TaskBoardBackEnd.dto.TaskReqResDto;
import com.example.TaskBoardBackEnd.model.Task;
import com.example.TaskBoardBackEnd.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TaskReqResDto>> getAllTaskById(@PathVariable Long id) {
        return taskService.getAllTasksById(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<TaskReqResDto> createTask(@RequestBody Task task, @PathVariable Long id) {
        return taskService.createTasks(task, id);
    }

    @PatchMapping("/{idTask}")
    public ResponseEntity<TaskReqResDto> editTask(@RequestBody Task task, @PathVariable Long idTask) {
        return taskService.editTasks(task, idTask);
    }

    @PatchMapping("/{idTask}/soft-delete")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long idTask) {
        return taskService.deleteById(idTask);
    }
}