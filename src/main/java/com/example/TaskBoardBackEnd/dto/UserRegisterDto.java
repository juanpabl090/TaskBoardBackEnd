package com.example.TaskBoardBackEnd.dto;

public record UserRegisterDto(
        String name,
        String userName,
        String email,
        String password
) {
}