package com.healthflow.healthflowbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // allow ALL paths
                .allowedOrigins("https://healthflow-frontend-difu.onrender.com")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); //frontend sends cookies or sessions
    }
}
