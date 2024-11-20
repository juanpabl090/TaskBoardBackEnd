package com.example.TaskBoardBackEnd.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    @JsonBackReference
    private User user;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @JsonProperty("ETaskStatus")
    private ETaskStatus ETaskStatus;
    private LocalDate createdAt;
    private LocalDate UpdatedAt;
    private String iconFileName;
    private boolean isDeleted;
}
