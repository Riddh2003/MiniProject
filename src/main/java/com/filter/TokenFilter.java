package com.filter;

import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.utility.JwtUtility;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter implements Filter {

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURL().toString();

        // Allow access to public and Swagger endpoints
        if (url.contains("/public/") || url.contains("/v3/") || url.contains("/swagger-resources/") 
            || url.contains("/swagger-ui/") || url.contains("/webjars/**")) {
            chain.doFilter(request, response);
            return;
        }

        String token = req.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove 'Bearer ' prefix
            System.out.println("Token from filter : " + token);

            if (jwtUtility.validateToken(token)) {
                String email = jwtUtility.validateTokenAndGetEmail(token);
                
                // Create authentication object with email AND authorities
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(new SimpleGrantedAuthority("admin"))  // Add admin authority
                    );

                // Set the authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("Email: " + email);

                chain.doFilter(request, response);
            } else {
                sendErrorResponse(response, "Invalid or expired token");
            }
        } else {
            sendErrorResponse(response, "Missing or invalid Authorization header");
        }
    }

    private void sendErrorResponse(ServletResponse response, String message) throws IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
