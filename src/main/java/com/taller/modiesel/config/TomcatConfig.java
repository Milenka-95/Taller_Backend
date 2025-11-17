package com.taller.modiesel.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to customize embedded Tomcat server
 * Hides server information to prevent fingerprinting
 */
@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            // Hide server version information
            connector.setProperty("server", "");
            // Disable X-Powered-By header
            connector.setProperty("xpoweredBy", "false");
        });
    }
}
