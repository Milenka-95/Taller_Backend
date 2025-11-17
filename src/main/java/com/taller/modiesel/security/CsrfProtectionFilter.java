package com.taller.modiesel.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF Protection Filter for REST API
 * Implements double-submit cookie pattern for stateless authentication
 */
@Component
public class CsrfProtectionFilter implements Filter {

    // Use header name that matches CORS configuration
    private static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";
    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final SecureRandom secureRandom = new SecureRandom();
    
    // Methods that don't modify state don't need CSRF protection
    private static final String[] SAFE_METHODS = {"GET", "HEAD", "OPTIONS", "TRACE"};
    
    // Public endpoints that don't need CSRF protection
    private static final String[] PUBLIC_PATHS = {
        "/api/auth/login",
        "/api/auth/register",
        "/api/productos",
        "/api/catalogo",
        "/swagger-ui",
        "/v3/api-docs"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestURI();
        
        // Check if this is a safe method or public path
        if (isSafeMethod(method) || isPublicPath(path)) {
            // Generate and send CSRF token for safe methods
            String csrfToken = generateCsrfToken();
            setCsrfCookie(httpResponse, csrfToken);
            // Also expose the token in a response header so frontend JS can read it
            httpResponse.setHeader(CSRF_TOKEN_HEADER, csrfToken);
            chain.doFilter(request, response);
            return;
        }

        // If the request includes an Authorization: Bearer <token>, we assume the client
        // is using Authorization header tokens stored in memory (recommended flow). In that
        // case CSRF risk is mitigated (the attacker can't make the browser attach an
        // Authorization header), so we skip CSRF validation here.
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // For state-changing methods without Authorization header, validate CSRF token
        String cookieToken = getCsrfTokenFromCookie(httpRequest);
        String headerToken = httpRequest.getHeader(CSRF_TOKEN_HEADER);

        // Strict double-submit: both cookie and header must be present and equal
        if (cookieToken != null && headerToken != null && cookieToken.equals(headerToken)) {
            // Token is valid; regenerate for next request
            String newToken = generateCsrfToken();
            setCsrfCookie(httpResponse, newToken);
            httpResponse.setHeader(CSRF_TOKEN_HEADER, newToken);
            chain.doFilter(request, response);
            return;
        }

        // Missing or mismatched token
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"error\": \"CSRF token validation failed\"}");
    }
    
    private boolean isSafeMethod(String method) {
        for (String safeMethod : SAFE_METHODS) {
            if (safeMethod.equals(method)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }
    
    private String generateCsrfToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    private void setCsrfCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(CSRF_COOKIE_NAME, token);
        cookie.setHttpOnly(false); // Must be readable by JavaScript when double-submit is used
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hour
        response.addCookie(cookie);
    }
    
    private String getCsrfTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CSRF_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
