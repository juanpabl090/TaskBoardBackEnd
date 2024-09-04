package com.example.TaskBoardBackEnd.service;

import com.example.TaskBoardBackEnd.dto.UserRegisterDto;
import com.example.TaskBoardBackEnd.model.ERole;
import com.example.TaskBoardBackEnd.model.RoleEntity;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(@Valid UserRegisterDto userRegisterDto) {
        if (userRepository.findUserByUserName(userRegisterDto.getName()).isPresent()
                && userRepository.findUserByEmail(userRegisterDto.getEmail()).isPresent()) {
            throw new Error("User already exists");
        }
        User user = User.builder()
                .name(userRegisterDto.getName())
                .userName(userRegisterDto.getUserName())
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .roles(Set.of(RoleEntity.builder().role(ERole.USER).build()))
                .build();
        userRepository.save(user);
    }

    public ResponseEntity<List<User>> getAllUsers() throws HttpClientErrorException.NotFound {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}