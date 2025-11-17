package com.taller.modiesel.config;
import com.taller.modiesel.external.ReniecApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiClientConfig {
    @Bean
    public ReniecApiClient reniecApiClient() {
        return new ReniecApiClient();
    }
}
