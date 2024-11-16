package com.ewallet.heropay.userservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private String email;

    @Min(10)
    private int age;
}
