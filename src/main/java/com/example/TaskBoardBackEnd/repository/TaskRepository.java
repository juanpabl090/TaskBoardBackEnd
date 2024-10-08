package com.example.TaskBoardBackEnd.repository;

import com.example.TaskBoardBackEnd.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM tasks t WHERE t.user.id = :userId")
    List<Task> findTasksByUserId(@Param("userId") Long userId);
}
