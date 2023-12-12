package com.example.microserviceone.intercepter;
import com.example.microserviceone.entity.ServiceOneEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ServiceOneIntercepter implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ServiceOneIntercepter.class);

    String error = "Error occured";
    Date requestTime = new Date(); // Capture the current date and time

    private long startTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        startTime = System.currentTimeMillis();
        logger.info("Pre-handling started");
        request.setAttribute("startTime", startTime);

        return true;
    }

    // completeblefuture is taking over here
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("After-complition started ");

        ServiceOneEntity serviceOneEntity = new ServiceOneEntity();

        //for time
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Date responseTime = new Date(); // Capture the current date and time for response
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //for response
        ContentCachingResponseWrapper wrapper;  // to get the response for every call of API
        logger.info("content caching for  fetching response");
        if (response instanceof ContentCachingResponseWrapper) {
            wrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapper = new ContentCachingResponseWrapper(response);
        }
        String responseContent = getResponse(wrapper);

        //for storing into database
        serviceOneEntity.setRequestTime(dateFormat.format(startTime));
        serviceOneEntity.setResponseTime(dateFormat.format(responseTime));
        serviceOneEntity.setStatusCode(response.getStatus());
        serviceOneEntity.setTimeTaken((timeTaken) + " ms");
        serviceOneEntity.setRequestURI(request.getRequestURI());
        serviceOneEntity.setRequestMethod(request.getMethod());
        serviceOneEntity.setRequestHeaderName(getRequestHeaderNames(request));
        serviceOneEntity.setContentType(request.getContentType());
        serviceOneEntity.setRequestID(generateRequestId());
        serviceOneEntity.setHostName(request.getServerName());
        serviceOneEntity.setResponse(responseContent);
        serviceOneEntity.setErrorTrace(errorStackTreeThread(ex));
        serviceOneEntity.setQueryParam(request.getQueryString());

        String clientId = request.getHeader("client_id");
        serviceOneEntity.setClientId(clientId);

        //webclient
        try {
            WebClient webClient = WebClient.create();
            logger.info("webclient executed");
            webClient.post()
//                .uri("http://localhost:7000/api/data")
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(7000)
                            .path("/api/data")
//                        .queryParam("")
                            .build())
                    .header("header","Header-value")
                    .body(BodyInserters.fromValue(serviceOneEntity))
                    .retrieve()
                    .bodyToMono(String.class)
                    .toFuture();
        } catch (WebClientResponseException clientException) {
            logger.error("Error while sending data using WebClient. HTTP Status: {}", clientException.getStatusCode());
            logger.error("Response body: {}", clientException.getResponseBodyAsString());
        } catch (Exception webClientException) {
            logger.error("Error while sending data using WebClient", webClientException);
        }
    }

    private String getRequestHeaderNames(HttpServletRequest request) {

        CompletableFuture <String> headerNameThread= CompletableFuture.supplyAsync(() -> {
            try {
                logger.info(" inside the Thread of getting header response");
                Enumeration<String> headerNames = request.getHeaderNames();
                StringBuilder headersStr = new StringBuilder();

                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    headersStr.append(headerName).append(": ");

                    Enumeration<String> headerValues = request.getHeaders(headerName);
                    while (headerValues.hasMoreElements()) {
                        String headerValue = headerValues.nextElement();
                        headersStr.append(headerValue).append(", ");
                    }
                    headersStr.delete(headersStr.length() - 2, headersStr.length());
                    headersStr.append(", ");
                }

                return headersStr.toString();
            }catch (Exception e) {
                logger.error("Error6 getting header name asynchronously", e);
                return error;
            }
        });
        logger.info(" header name Thread executed");
        return headerNameThread.join();
    }

    public String getResponse(ContentCachingResponseWrapper contentCachingResponseWrapper) {
        logger.info("Getting response asynchronously");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("Getting response");
                return  IOUtils.toString(contentCachingResponseWrapper.getContentAsByteArray(),
                        contentCachingResponseWrapper.getCharacterEncoding());
            } catch (Exception e) {
                logger.error("Error getting response asynchronously", e);
                return error;
            }
        });
        logger.info("thread executed");
        return future.join();
    }

    public static String generateRequestId() {
        UUID uuid = UUID.randomUUID();
        logger.info("Generating alphanumaric request ID ");
        String string = uuid.toString().replace("-", ""); // Remove hyphens
        StringBuilder alphanumericCharacters = new StringBuilder(string.replaceAll("[^A-Za-z0-9]", "")); // Remove non-alphanumeric characters
        while (alphanumericCharacters.length() < 10) {
            alphanumericCharacters.append(generateRandomAlphanumeric());
        }
        return alphanumericCharacters.substring(0, 10);
    }

    private static String generateRandomAlphanumeric() {
        logger.info(" inside the generateRandomAlphanumeric method");
        CompletableFuture <String> aplha= CompletableFuture.supplyAsync(() -> {
            logger.info(" generateRandomAlphanumeric thread started");
            try {
                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                int randomIndex = (int) (Math.random() * characters.length());
                return characters.substring(randomIndex, randomIndex + 1);
            } catch (Exception e) {
                logger.error("Error getting alpha numaric ID asynchronously", e);
                return "Error occurred";
            }
        });
        logger.info(" generateRandomAlphanumeric thread completed");
        return aplha.join();
    }

    public String errorStackTreeThread(Exception ex){
        logger.info("inside the errorStackThread");
        CompletableFuture <String> errorThread = CompletableFuture.supplyAsync(()-> {
            try {

                String errorStackTrace = null;
                logger.info("error trace ");
                if (ex != null) {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    errorStackTrace = sw.toString();
                }
                return errorStackTrace;
            }catch (Exception e) {
                logger.error("Error getting errorStackTrace asynchronously", e);
                return "Error occurred";
            }
        });
        logger.info("Thread executed");
        return errorThread.join();
    }
}