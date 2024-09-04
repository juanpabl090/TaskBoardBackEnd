package com.example.TaskBoardBackEnd.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task", nullable = false)
    private Long idTask;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private ETaskStatus ETaskStatus;
    private LocalDate createdAt;
    private LocalDate UpdatedAt;
}
