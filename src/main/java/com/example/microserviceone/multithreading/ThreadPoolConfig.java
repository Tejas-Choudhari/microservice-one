//package com.example.microserviceone.multithreading;
//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableAsync
//public class ThreadPoolConfig {
//
//    @Bean(name = "asyncExecutor")
//    public ThreadPoolTaskExecutor myThreadPoolTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("MyThread-");
//        executor.initialize();
//        return executor;
//    }
//}
