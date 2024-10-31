package com.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getRequestURL().toString();

        // Allow public endpoints without authentication
        if (url.contains("/public/")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract the token from the Authorization header
        String token = req.getHeader("Authorization");
        
        // Check for a valid token
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove 'Bearer ' prefix

            if (jwtUtility.validateToken(token)) {
                String email = jwtUtility.validateTokenAndGetEmail(token);
                
                // Create authentication object with user email
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(email, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                // Set the authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Continue the filter chain
                chain.doFilter(request, response);
            } else {
                handleUnauthorized(res, "Invalid or expired token");
            }
        } else {
            handleUnauthorized(res, "Missing or invalid Authorization header");
        }
    }

    private void handleUnauthorized(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
