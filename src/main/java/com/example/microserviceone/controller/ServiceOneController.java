package com.example.microserviceone.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceOneController {


    @GetMapping("/name")
    public String getName(){
        return "API-1 Called from Microservices One";
    }

    @GetMapping("/post")
    public String getPost(){
        return "API-2 Called from Miceroservices One";
    }
}
