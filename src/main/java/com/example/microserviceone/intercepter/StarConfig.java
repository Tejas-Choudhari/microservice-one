package com.example.microserviceone.intercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//class to configuration of the intercepter that what controller should implement the intercepter
@Configuration
public class StarConfig implements WebMvcConfigurer {

    @Bean
    public ServiceOneIntercepter serviceOneIntercepter() {
        return new ServiceOneIntercepter();
    }

//    @Autowired
//    private ServiceOneIntercepter serviceOneIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceOneIntercepter());

    }

}

