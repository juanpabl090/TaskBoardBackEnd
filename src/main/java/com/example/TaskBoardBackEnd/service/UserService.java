package com.example.TaskBoardBackEnd.service;

import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void logout() {
        SecurityContextHolder.clearContext();
        System.out.println(SecurityContextHolder.getContext().toString());
    }

    public ResponseEntity<List<User>> getAllUsers() throws HttpClientErrorException.NotFound {
        return ResponseEntity.ok(userRepository.findAll().stream().toList());
    }

    public ResponseEntity<Optional<User>> getUserById(Long id) throws HttpClientErrorException.NotFound {
        return ResponseEntity.ok(userRepository.findById(id));
    }
}