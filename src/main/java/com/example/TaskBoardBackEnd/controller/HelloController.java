package com.example.TaskBoardBackEnd.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/home")
    public String homePage() {
        return "Home Page";
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