package org.example.liquorice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private String id;
    private LocalDateTime createdDate;
    private double totalAmount;
    private String status;
    private LocalDate estimatedDeliveryDate;
    private List<OrderItemDto> orderItems;
}
