package org.example.liquorice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    ProductPreviewDto product;
    int quantity;
}
