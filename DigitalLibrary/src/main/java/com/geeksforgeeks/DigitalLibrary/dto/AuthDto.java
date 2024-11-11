package com.geeksforgeeks.DigitalLibrary.dto;

import lombok.*;

@With
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private String username;
    private String password;
}
