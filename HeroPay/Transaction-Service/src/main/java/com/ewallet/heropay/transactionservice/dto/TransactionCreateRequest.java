package com.ewallet.heropay.transactionservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreateRequest {
    @NotBlank
    private String senderId;

    @NotBlank
    private String receiverId;

    @Min(1)
    private Long amount;

    private String reason;
}
