package com.taller.modiesel.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // X-Content-Type-Options: Prevents MIME type sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        // X-Frame-Options: Prevents clickjacking attacks
        httpResponse.setHeader("X-Frame-Options", "DENY");
        
        // Strict-Transport-Security (HSTS): Forces HTTPS
        // Note: Only enable if your application is fully HTTPS
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        
        // Referrer-Policy: Controls referrer information
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Content-Security-Policy: Mitigates XSS and other injection attacks
        // Starting with a restrictive policy - adjust based on your needs
        httpResponse.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self'; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'; " +
            "base-uri 'self'; " +
            "form-action 'self'");
        
        // X-Permitted-Cross-Domain-Policies: Restricts cross-domain policies
        httpResponse.setHeader("X-Permitted-Cross-Domain-Policies", "none");
        
        // Cache-Control: Prevents caching of sensitive data
        // This will be set on specific endpoints, but we set a default here
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setHeader("Expires", "0");
        
        chain.doFilter(request, response);
    }
}
