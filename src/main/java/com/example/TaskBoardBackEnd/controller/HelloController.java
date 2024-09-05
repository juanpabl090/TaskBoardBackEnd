package com.example.TaskBoardBackEnd.controller;

import com.example.TaskBoardBackEnd.model.RoleEntity;
import com.example.TaskBoardBackEnd.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HelloController {
    private final RoleRepository roleRepository;

    @Autowired
    public HelloController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/home/{id}")
    public RoleEntity homePage(@PathVariable Long id) {
        RoleEntity roleName = roleRepository.findRoleNameById(id);
        return roleName;
    }

    @GetMapping("/greetingAuth")
    public String helloWorld() {
        return "Hello world";
    }

    @GetMapping("/testUser")
    public String testUser() {
        return "testUser";
    }

    @GetMapping("/testAdmin")
    public String testAdmin() {
        return "testAdmin";
    }

    @GetMapping("/testGet")
    public String testGet() {
        return "testGet";
    }

    @PostMapping("/testPOST")
    public String testPOST() {
        return "testPOST";
    }

    @DeleteMapping("/testDELETE")
    public String testDELETE() {
        return "testDELETE";
    }

    @PutMapping("/testPUT")
    public String testPUT() {
        return "testPUT";
    }
}