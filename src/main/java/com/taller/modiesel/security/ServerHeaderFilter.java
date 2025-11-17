package com.taller.modiesel.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to remove or obscure server identification headers
 * that could be used for fingerprinting
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServerHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            
            // Remove Server header - this is handled by server configuration
            // but we ensure it's not set by the application
            httpResponse.setHeader("Server", "");
            
            // Remove X-Powered-By if present
            httpResponse.setHeader("X-Powered-By", "");
        }
        
        chain.doFilter(request, response);
    }
}
