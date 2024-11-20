package com.example.TaskBoardBackEnd.service;

import com.example.TaskBoardBackEnd.dto.TaskReqResDto;
import com.example.TaskBoardBackEnd.model.ETaskStatus;
import com.example.TaskBoardBackEnd.model.Task;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.TaskRepository;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private static TaskReqResDto convertToDto(Task task) {
        return TaskReqResDto.builder()
                .idTask(task.getIdTask())
                .title(task.getTitle())
                .description(task.getDescription())
                .ETaskStatus(task.getETaskStatus())
                .createdAt(task.getCreatedAt())
                .UpdatedAt(task.getUpdatedAt())
                .iconFileName(task.getIconFileName())
                .build();
    }

    public ResponseEntity<TaskReqResDto> createTasks(Task task, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (task.getTitle().isEmpty() || task.getDescription().isEmpty() || task.getIconFileName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Task newTask = Task.builder()
                .user(user)
                .title(task.getTitle())
                .description(task.getDescription())
                .ETaskStatus(ETaskStatus.InProgress)
                .createdAt(LocalDate.now())
                .UpdatedAt(LocalDate.now())
                .iconFileName(task.getIconFileName())
                .build();
        Task savedTask = taskRepository.save(newTask);
        TaskReqResDto taskReqResDto = convertToDto(savedTask);
        return ResponseEntity.ok(taskReqResDto);
    }

    public ResponseEntity<List<TaskReqResDto>> getAllTasksById(Long id) {
        List<Task> tasks = taskRepository.findTasksByUserId(id);
        Optional<List<TaskReqResDto>> taskDto = Optional.of(tasks.stream()
                .filter(task -> !task.isDeleted())
                .map(TaskService::convertToDto)
                .collect(Collectors.toList()));
        return ResponseEntity.of(taskDto);
    }

    public ResponseEntity<TaskReqResDto> editTasks(Task task, Long idTask) {
        Task existingTask = taskRepository.findById(idTask).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        System.out.println("Estado recibido" + task.getETaskStatus());
        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            existingTask.setTitle(task.getTitle());
        }
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            existingTask.setDescription(task.getDescription());
        }
        if (task.getETaskStatus() != null) {
            existingTask.setETaskStatus(task.getETaskStatus());
        }
        if (task.getIconFileName() != null && !task.getIconFileName().isEmpty()) {
            existingTask.setIconFileName(task.getIconFileName());
        }
        existingTask.setUpdatedAt(LocalDate.now());
        Task savedTask = taskRepository.save(existingTask);
        TaskReqResDto taskReqResDto = convertToDto(savedTask);
        return ResponseEntity.ok(taskReqResDto);
    }

    @Transactional
    public ResponseEntity<Void> deleteById(Long idTask) {
        Task existingTask = taskRepository.findById(idTask).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (!existingTask.isDeleted()) {
            existingTask.setDeleted(true);
            taskRepository.save(existingTask);
        }
        return ResponseEntity.ok().build();
    }
}
