package com.example.microserviceone.intercepter;

import com.example.microserviceone.entity.ServiceOneEntity;
//import com.example.microserviceone.service.ServiceOneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ServiceOneIntercepter implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceOneIntercepter.class);


    private WebClient.Builder builder;

    Date requestTime = new Date(); // Capture the current date and time

    private long startTime;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        startTime = System.currentTimeMillis();
        logger.info("Pre-handling started");
        Date requestTime = new Date(); // Capture the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Request Time: " + dateFormat.format(requestTime));
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        logger.info("After-complition started ");
        ServiceOneEntity serviceOneEntity= new ServiceOneEntity();

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Date responseTime = new Date(); // Capture the current date and time for response
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //for error trace
        String errorStackTrace = null;
        logger.info("error trace ");
        if (ex != null) {
            // Capture the exception stack trace in a variable
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            errorStackTrace = sw.toString();
            System.out.println(" error trace : " + errorStackTrace);
        }


        //for response
        ContentCachingResponseWrapper wrapper;
        logger.info("content caching for  fetching response");
        if (response instanceof ContentCachingResponseWrapper) {
            wrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapper = new ContentCachingResponseWrapper(response);
        }
        String responseContent = getResponse(wrapper);

        //for query param
        Map<String, String[]> queryParams = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            String paramValue = (paramValues != null && paramValues.length > 0) ? paramValues[0] : null;

            logger.info("Query Parameter: {} = {}", paramName, paramValue);

            serviceOneEntity.setQueryParam(paramValue);

            // You can store or process the query parameter as needed
        }



        //for storing into database
        serviceOneEntity.setRequestTime(dateFormat.format(startTime));
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
        logger.info("webclient executed");
        webClient.post()
                .uri("http://localhost:7000/api/data")
                .body(BodyInserters.fromValue(serviceOneEntity))
                .retrieve()
                .bodyToMono(String.class)
                .block();


    }


    private String getRequestHeaderNames(HttpServletRequest request) {
        logger.info("getting header response");
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headerNamesStr = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerNamesStr.append(headerName).append(", ");
        }
        return headerNamesStr.toString();
    }

    private String getResponse(ContentCachingResponseWrapper contentCachingResponseWrapper) {
        logger.info(" getting response");
        String response = IOUtils.toString(contentCachingResponseWrapper.getContentAsByteArray(), contentCachingResponseWrapper.getCharacterEncoding());
        return response;
    }

    public static String generateRequestId() {
        UUID uuid = UUID.randomUUID();
        logger.info("Generating alphanumaric request ID ");
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
