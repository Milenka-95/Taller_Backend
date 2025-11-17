package com.taller.modiesel.config;
import com.taller.modiesel.security.JwtRequestFilter;
import com.taller.modiesel.security.SecurityHeadersFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class SecurityConfig {


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private SecurityHeadersFilter securityHeadersFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Configure CSRF protection with token repository
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setCookieName("XSRF-TOKEN");
        tokenRepository.setHeaderName("X-XSRF-TOKEN");
        
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
                // Enable CSRF protection with cookie-based token
                .csrf(csrf -> csrf
                    .csrfTokenRepository(tokenRepository)
                    .csrfTokenRequestHandler(requestHandler)
                    // Ignore CSRF for public read-only endpoints
                    .ignoringRequestMatchers(
                        "/api/auth/login",
                        "/api/productos/listar",
                        "/api/catalogo/**"
                    )
                )
                .authorizeHttpRequests(auth -> auth
                    // Public endpoints
                    .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                    .requestMatchers("/api/productos/listar", "/api/catalogo/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    // Protected endpoints
                    .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                    .requestMatchers("/api/ventas/**", "/api/inventario/**").hasAnyRole("ADMIN", "EMPLEADO")
                    .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sess -> sess
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    // Enable session fixation protection
                    .sessionFixation().migrateSession()
                )
                // Configure secure cookies
                .headers(headers -> headers
                    .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)
                    )
                );

        // Add security filters
        http.addFilterBefore(securityHeadersFilter, BasicAuthenticationFilter.class);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use BCrypt instead of NoOpPasswordEncoder for security
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
