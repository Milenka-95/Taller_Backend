package com.taller.modiesel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value("${cors.allowed.origins:http://localhost:3000}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // Use specific origins - NO wildcards for security
                        // Configure allowed origins via application.properties
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        // Specify allowed headers instead of wildcard
                        .allowedHeaders("Authorization", "Content-Type", "X-CSRF-Token", "Accept")
                        .allowCredentials(true)
                        .exposedHeaders("Authorization", "X-CSRF-Token")
                        .maxAge(3600);
            }
        };
    }
}