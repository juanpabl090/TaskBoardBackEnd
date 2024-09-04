package com.example.TaskBoardBackEnd.repository;

import com.example.TaskBoardBackEnd.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
