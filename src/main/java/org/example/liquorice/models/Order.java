package org.example.liquorice.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@Document(collection = "orders")
public class Order {
    private String id;
    private String customerId;
    private Map<String, Integer> productQuantity;
    private double totalAmount;
    private Status status;
    @CreatedDate
    private LocalDateTime createdDate;
    private LocalDate estimatedDeliveryDate;
    private String paymentIntentId;


    public enum Status {
        CREATED,
        PROCESSING,
        SHIPPING,
        DELIVERED,
        ANNULLED,
        REFUNDED
    }
}
