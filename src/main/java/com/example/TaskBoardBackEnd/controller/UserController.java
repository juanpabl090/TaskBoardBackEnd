package com.example.TaskBoardBackEnd.controller;

import com.example.TaskBoardBackEnd.dto.AuthRequestDto;
import com.example.TaskBoardBackEnd.dto.AuthResponseDto;
import com.example.TaskBoardBackEnd.dto.UserRegisterDto;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.service.UserDetailServiceImpl;
import com.example.TaskBoardBackEnd.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {
    private final UserService userService;
    private final UserDetailServiceImpl userDetailService;


    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, UserDetailServiceImpl userDetailService) {
        this.userService = userService;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        return userDetailService.registerUser(userRegisterDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return userDetailService.loginUser(authRequestDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().getBody());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUserById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id).getBody());
    }
}