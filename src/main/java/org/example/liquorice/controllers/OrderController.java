package org.example.liquorice.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.AppConfig;
import org.example.liquorice.dtos.*;
import org.example.liquorice.exceptions.NotFoundException;
import org.example.liquorice.models.Order;
import org.example.liquorice.services.CartService;
import org.example.liquorice.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConfig.BASE_PATH + "/orders")
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/complete")
    public ResponseEntity<OrderResponseDto> completeOrder(Authentication authentication, @RequestBody OrderRequestDto orderRequest) throws StripeException {
        return orderService.submitOrder(authentication.getName(), orderRequest)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Order or customer not found"));
    }

    @PostMapping
    public ResponseEntity<ClientIntentResponseDto> createOrder(Authentication authentication, @RequestBody AddressDto addressDto) throws StripeException {
        CartResponseDto cart = cartService.getCart(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        double totalAmount = cartService.getTotalPrice(cart);

        PaymentIntent paymentIntent = orderService.generatePaymentIntent((int) (totalAmount * 100));

        Order order = orderService.createOrder(authentication.getName(), cart, paymentIntent.getId(), addressDto)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return ResponseEntity.ok(new ClientIntentResponseDto(
                paymentIntent.getClientSecret(),
                order.getId())
        );
    }

    @GetMapping("/{orderId}/payment-intent")
    public ResponseEntity<ClientIntentResponseDto> getPaymentIntent(@PathVariable String orderId) throws StripeException {
        PaymentIntent paymentIntent = orderService.getPaymentIntent(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        return ResponseEntity.ok(new ClientIntentResponseDto(paymentIntent.getClientSecret(), orderId));
    }

    @PatchMapping("/{orderId}/refund")
    public ResponseEntity<OrderResponseDto> refundOrder(@PathVariable String orderId) throws StripeException {
        return orderService.refundOrder(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }
}
