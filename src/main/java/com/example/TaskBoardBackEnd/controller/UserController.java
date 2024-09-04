package com.example.TaskBoardBackEnd.controller;

import com.example.TaskBoardBackEnd.dto.UserRegisterDto;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDto> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        userService.registerUser(userRegisterDto);
        return ResponseEntity.ok(userRegisterDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        ResponseEntity<List<User>> user = userService.getAllUsers();
        return ResponseEntity.ok(user.getBody());
    }
}
