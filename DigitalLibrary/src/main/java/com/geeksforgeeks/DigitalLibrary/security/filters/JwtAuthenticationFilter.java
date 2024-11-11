package com.geeksforgeeks.DigitalLibrary.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforgeeks.DigitalLibrary.dto.AuthDto;
import com.geeksforgeeks.DigitalLibrary.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Parse the incoming JSON request into an AuthDto object
            AuthDto authDto = new ObjectMapper().readValue(request.getInputStream(), AuthDto.class);
            // Create an authentication token using the username and password from AuthDto
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
            // Authenticate the token using the AuthenticationManager
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // Retrieve the authenticated user's details
        User user = (User) authResult.getPrincipal();
        // Generate a JWT token for the authenticated user
        String jwtToken = this.jwtUtil.generateToken(user);
        // Set the response content type as JSON and write the token to the response
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");
    }
}
