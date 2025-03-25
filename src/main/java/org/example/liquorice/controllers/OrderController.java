package org.example.liquorice.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.AppConfig;
import org.example.liquorice.dtos.*;
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
    public ResponseEntity<OrderResponseDto> completeOrder(Authentication authentication, @RequestBody OrderRequestDto orderRequest) throws StripeException {
        OrderResponseDto order = orderService.submitOrder(authentication.getName(), orderRequest);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<ClientIntentResponseDto> createOrder(Authentication authentication, @RequestBody AddressDto addressDto) throws StripeException {
        CartResponseDto cart = cartService.getCart(authentication.getName());
        double totalAmount = cartService.getTotalPrice(cart);

        PaymentIntent paymentIntent = orderService.generatePaymentIntent((int) (totalAmount * 100));

        return ResponseEntity.ok(new ClientIntentResponseDto(
                paymentIntent.getClientSecret(),
                orderService.createOrder(authentication.getName(), cart, paymentIntent.getId(), addressDto).getId())
        );
    }

    @GetMapping("/{orderId}/payment-intent")
    public ResponseEntity<ClientIntentResponseDto> getPaymentIntent(@PathVariable String orderId) throws StripeException {
        PaymentIntent paymentIntent = orderService.getPaymentIntent(orderId);
        return ResponseEntity.ok(new ClientIntentResponseDto(paymentIntent.getClientSecret(), orderId));
    }
}