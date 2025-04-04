package org.example.liquorice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.dtos.CartRequestDto;
import org.example.liquorice.dtos.CartResponseDto;
import org.example.liquorice.exceptions.NotFoundException;
import org.example.liquorice.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.example.liquorice.config.AppConfig.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

   @PostMapping
    public ResponseEntity<CartResponseDto> saveCart(@RequestBody CartRequestDto cart, Authentication authentication) {
        return cartService.saveCart(cart, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
