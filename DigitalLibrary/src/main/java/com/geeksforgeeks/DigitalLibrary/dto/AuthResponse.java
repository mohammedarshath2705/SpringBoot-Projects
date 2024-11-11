package com.geeksforgeeks.DigitalLibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
public class AuthResponse {
    private UserDetails user;
    private String token;
}