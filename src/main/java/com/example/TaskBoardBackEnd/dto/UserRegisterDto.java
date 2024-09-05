package com.example.TaskBoardBackEnd.dto;

import com.example.TaskBoardBackEnd.model.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserRegisterDto {
    @NotNull(message = "name is required")
    String name;
    @NotNull(message = "name is required")
    String userName;
    @NotNull(message = "Email is required")
    @Email(message = "invalid Email address")
    String email;
    @NotNull(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 letters")
    String password;
}