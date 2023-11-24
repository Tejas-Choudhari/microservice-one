package com.example.microserviceone.controller;


import com.example.microserviceone.intercepter.ServiceOneIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ServiceOneController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceOneController.class);


    @GetMapping("/name")
    public String getName(@RequestParam String queryParam){
        logger.info("inside the /name API");
        return "API-1 Called from Microservices One";
    }

    @GetMapping("/post")
    public String getPost(){
        logger.info(" inside the /post API");
        return "API-2 Called from Miceroservices One";
    }


    @PostMapping("/method")
    public String postMethod(){
        logger.info(" Inside the /method API");
        return "Post Method from microservice one is called ";
    }


    @DeleteMapping("/del")
    public String delMethod(){
        logger.info("inside the /del API ");
        return "Delete method is called from microservices one";
    }
}
