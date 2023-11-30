package com.example.microserviceone.controller;


import com.example.microserviceone.converter.DtoConverter;
import com.example.microserviceone.dto.ServiceOneDto;
import com.example.microserviceone.entity.ServiceOneEntity;
import com.example.microserviceone.intercepter.ServiceOneIntercepter;
import com.example.microserviceone.repo.ServiceOneRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ServiceOneController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceOneController.class);
    @Autowired
    private ServiceOneRepo serviceOneRepo;

    @Autowired
    private DtoConverter dtoConverter;


    @GetMapping("/name")
    public String getName(@RequestHeader Map<String ,String> header, @RequestParam String queryParam ,@RequestParam int age) {
        logger.info("inside the /name API");
        return "API-1 Called from Microservices One";
    }

    @GetMapping("/post")
    public String getPost(@RequestHeader Map<String ,String>header) {
        logger.info(" inside the /post API");
        return "API-2 Called from Miceroservices One";
    }

    @GetMapping("/findAll")
    public List<ServiceOneDto> findAll(@RequestHeader Map<String ,String>header) {
        List<ServiceOneEntity> findAll = serviceOneRepo.findAll();
        return dtoConverter.entitiesToDtos(findAll);
    }


    @PostMapping("/method")
    public String postMethod(@RequestHeader Map<String ,String>header) {
        logger.info(" Inside the /method API");
        return "Post Method from microservice one is called ";
    }


    @DeleteMapping("/del")
    public String delMethod(@RequestHeader Map<String ,String>header) {
        logger.info("inside the /del API ");
        return "Delete method is called from microservices one";
    }
}
