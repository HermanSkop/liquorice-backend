package org.example.liquorice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDto {
    ProductPreviewDto product;
    int quantity;
}
