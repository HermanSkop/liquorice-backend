package org.example.liquorice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientIntentResponseDto {
    private String clientSecret;
    private String orderId;
}
