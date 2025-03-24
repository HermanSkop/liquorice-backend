package org.example.liquorice.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.AppConfig;
import org.example.liquorice.dtos.CartResponseDto;
import org.example.liquorice.dtos.ClientIntentResponseDto;
import org.example.liquorice.dtos.OrderRequestDto;
import org.example.liquorice.dtos.OrderResponseDto;
import org.example.liquorice.services.CartService;
import org.example.liquorice.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConfig.BASE_PATH + "/orders")
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/complete")
    public ResponseEntity<OrderResponseDto> createOrder(Authentication authentication, @RequestBody OrderRequestDto orderRequest) throws StripeException {
        OrderResponseDto order = orderService.submitOrder(authentication.getName(), orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/init-order")
    public ResponseEntity<ClientIntentResponseDto> initOrder(Authentication authentication) throws StripeException {
        CartResponseDto cart = cartService.getCart(authentication.getName());
        double totalAmount = cartService.getTotalPrice(cart);

        PaymentIntent paymentIntent = orderService.generatePaymentIntent((int) (totalAmount * 100));

        return ResponseEntity.ok(new ClientIntentResponseDto(
                paymentIntent.getClientSecret(),
                orderService.createOrder(authentication.getName(), cart, paymentIntent.getId()).getId())
        );
    }
}