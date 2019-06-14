package com.dapinder.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/home")
    public String home(){
        return "Welcome to Dapinder Web Home!!";
    }

    @GetMapping("/health")
    public String health(){
        return "up";
    }
}
