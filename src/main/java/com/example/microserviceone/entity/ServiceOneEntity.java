package com.example.microserviceone.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "m1_audit_data")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serviceName = "microservice-one";

    //extra fields
    private String requestTime;
    private String responseTime;
    private int statusCode;
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
    private String clientId;
}
