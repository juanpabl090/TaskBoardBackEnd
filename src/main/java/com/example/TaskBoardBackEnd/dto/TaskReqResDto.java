package com.example.TaskBoardBackEnd.dto;

import com.example.TaskBoardBackEnd.model.ETaskStatus;
import com.example.TaskBoardBackEnd.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskReqResDto(
        @NotNull
        Long idTask,
        @JsonIgnore
        User user,
        String title,
        String description,
        ETaskStatus ETaskStatus,
        LocalDate createdAt,
        LocalDate UpdatedAt,
        String iconFileName
) {
}