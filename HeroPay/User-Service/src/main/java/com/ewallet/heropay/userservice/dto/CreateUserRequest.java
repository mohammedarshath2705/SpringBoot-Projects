package com.ewallet.heropay.userservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private String email;

    @Min(10)
    private int age;
}
