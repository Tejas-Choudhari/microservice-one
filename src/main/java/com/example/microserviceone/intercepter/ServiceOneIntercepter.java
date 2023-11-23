package com.example.microserviceone.intercepter;

import com.example.microserviceone.entity.ServiceOneEntity;
//import com.example.microserviceone.service.ServiceOneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

@Component
@Slf4j
public class ServiceOneIntercepter implements HandlerInterceptor {

    private WebClient.Builder builder;

//    @Autowired
//    private ServiceOneService serviceOneService;

    Date requestTime = new Date(); // Capture the current date and time

    private long startTime;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        startTime = System.currentTimeMillis();
        Date requestTime = new Date(); // Capture the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Request Time: " + dateFormat.format(requestTime));
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        ServiceOneEntity serviceOneEntity= new ServiceOneEntity();

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Date responseTime = new Date(); // Capture the current date and time for response
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //for error trace
        String errorStackTrace = null;
        if (ex != null) {
            // Capture the exception stack trace in a variable
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            errorStackTrace = sw.toString();
            System.out.println(" error trace : " + errorStackTrace);
        }


        //for response
        ContentCachingResponseWrapper wrapper;
        if (response instanceof ContentCachingResponseWrapper) {
            wrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapper = new ContentCachingResponseWrapper(response);
        }
        String responseContent = getResponse(wrapper);



        //for storing into database
        serviceOneEntity.setRequestTime(dateFormat.format(requestTime));
        serviceOneEntity.setResponseTime(dateFormat.format(responseTime));
        serviceOneEntity.setStatusCode(response.getStatus());
        serviceOneEntity.setTimeTaken(String.valueOf(timeTaken)+" ms");
        serviceOneEntity.setRequestURI(request.getRequestURI());
        serviceOneEntity.setRequestMethod(request.getMethod());
        serviceOneEntity.setRequestHeaderName(getRequestHeaderNames(request));
        serviceOneEntity.setContentType(request.getContentType());
        serviceOneEntity.setRequestID(generateRequestId());
        serviceOneEntity.setHostName(request.getServerName());
        serviceOneEntity.setResponse(responseContent);
        serviceOneEntity.setErrorTrace(errorStackTrace);

//        serviceOneService.saveEntity(serviceOneEntity);


        WebClient webClient = WebClient.create();
        webClient.post()
                .uri("http://localhost:7000/api/data")
                .body(BodyInserters.fromValue(serviceOneEntity))
                .retrieve()
                .bodyToMono(String.class)
                .block();


    }


    private String getRequestHeaderNames(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headerNamesStr = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerNamesStr.append(headerName).append(", ");
        }
        return headerNamesStr.toString();
    }

    private String getResponse(ContentCachingResponseWrapper contentCachingResponseWrapper) {

        String response = IOUtils.toString(contentCachingResponseWrapper.getContentAsByteArray(), contentCachingResponseWrapper.getCharacterEncoding());
        return response;
    }

    public static String generateRequestId() {
        UUID uuid = UUID.randomUUID();
        String string = uuid.toString().replaceAll("-", ""); // Remove hyphens
        String alphanumericCharacters = string.replaceAll("[^A-Za-z0-9]", ""); // Remove non-alphanumeric characters
//        int randomIndex = (int) (Math.random() * alphanumericCharacters.length());

        while (alphanumericCharacters.length() < 10) {
            alphanumericCharacters += generateRandomAlphanumeric();
        }

        return alphanumericCharacters.substring(0, 10);
    }

    private static String generateRandomAlphanumeric() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int randomIndex = (int) (Math.random() * characters.length());
        return characters.substring(randomIndex, randomIndex + 1);
    }
}
