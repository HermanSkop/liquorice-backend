package org.example.liquorice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDto {
    List<CartItemDto> cartItems;
}
