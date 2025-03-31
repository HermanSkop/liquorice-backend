package org.example.liquorice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.AppConfig;
import org.example.liquorice.dtos.OrderResponseDto;
import org.example.liquorice.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConfig.BASE_PATH + "/customers")
public class CustomerController {
    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getOrders(Authentication authentication) {
        List<OrderResponseDto> orders = orderService.getOrdersForCustomer(authentication.getName());
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<List<OrderResponseDto>> getCustomerOrders(Authentication authentication, @PathVariable String customerId) {
        List<OrderResponseDto> orders = orderService.getOrdersForCustomerById(customerId);
        return ResponseEntity.ok(orders);
    }
}
