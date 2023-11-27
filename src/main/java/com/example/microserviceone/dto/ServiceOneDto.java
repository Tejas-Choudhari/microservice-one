package com.example.microserviceone.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

@Data
public class ServiceOneDto {
    private String serviceName = "microservice-one";
    private long id;

    //extra fields
    private String requestTime;
    private String responseTime;
    private int StatusCode;
    private String timeTaken;
    private String requestURI;
    private String requestMethod;
    private String requestHeaderName;
    private String contentType;
    private String queryParam;
    private String requestID;
    private String hostName;
    private String response;
    private String errorTrace;
    private String client_id;
}
