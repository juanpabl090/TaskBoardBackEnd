package com.example.TaskBoardBackEnd.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HelloController {
    @GetMapping("/hello")
    public String helloWorld() {
        return "hello world";
    }
}