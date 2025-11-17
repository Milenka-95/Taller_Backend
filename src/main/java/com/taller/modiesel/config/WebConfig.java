package com.taller.modiesel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    // Configure allowed origins from application properties
    @Value("${cors.allowed.origins:http://localhost:3000}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // Use specific origins instead of wildcards
                        .allowedOrigins(allowedOrigins.split(","))
                        // Restrict allowed methods to only what's needed
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Restrict allowed headers
                        .allowedHeaders("Content-Type", "Authorization", "X-XSRF-TOKEN", "X-Requested-With")
                        // Enable credentials (required for CSRF cookies)
                        .allowCredentials(true)
                        // Expose necessary headers
                        .exposedHeaders("Authorization", "X-XSRF-TOKEN")
                        // Cache preflight response
                        .maxAge(3600);
            }
        };
    }
}