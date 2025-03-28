package org.example.liquorice.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.example.liquorice.dtos.*;
import org.example.liquorice.models.Address;
import org.example.liquorice.models.Order;
import org.example.liquorice.models.User;
import org.example.liquorice.repositories.OrderRepository;
import org.example.liquorice.repositories.ProductRepository;
import org.example.liquorice.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public OrderResponseDto submitOrder(String customerEmail, OrderRequestDto orderRequest) throws StripeException {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = orderRepository.findById(orderRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!Objects.equals(order.getCustomerId(), customer.getId()))
            throw new IllegalArgumentException("Customer does not own this order");

        PaymentIntent paymentIntent = PaymentIntent.retrieve(order.getPaymentIntentId());
        String paymentStatus = paymentIntent.getStatus();
        if (!"succeeded".equals(paymentStatus))
            throw new IllegalStateException("Payment not completed. Current status: " + paymentStatus);

        order.setStatus(Order.Status.PROCESSING);

        return mapToOrderResponseDto(orderRepository.save(order));
    }

    public Order createOrder(String customerEmail, CartResponseDto cart, String paymentIntentId, AddressDto address) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = Order.builder()
                .customerId(customer.getId())
                .totalAmount(cartService.getTotalPrice(cart))
                .status(Order.Status.CREATED)
                .productQuantity(cart.getCartItems()
                        .stream()
                        .collect(Collectors.toMap(
                                cartItem -> cartItem.getProduct().getId(),
                                CartItemDto::getQuantity)
                        )
                )
                .deliveryAddress(modelMapper.map(address, Address.class))
                .paymentIntentId(paymentIntentId)
                .estimatedDeliveryDate(LocalDate.now().plusDays(7))
                .build();

        return orderRepository.save(order);
    }

    public PaymentIntent generatePaymentIntent(int amountCents) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amountCents);
        params.put("currency", "usd");
        params.put("automatic_payment_methods", Map.of("enabled", true));
        return PaymentIntent.create(params);
    }

    public OrderResponseDto mapToOrderResponseDto(Order order) {
        OrderResponseDto dto = modelMapper.map(order, OrderResponseDto.class);
        dto.setOrderItems(
                order.getProductQuantity()
                        .keySet()
                        .stream()
                        .map(productId -> productRepository.findById(productId).orElse(null))
                        .filter(Objects::nonNull)
                        .map(product -> new OrderItemDto(
                                productService.mapToProductPreviewDto(product),
                                order.getProductQuantity().get(product.getId()))
                        )
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public List<OrderResponseDto> getOrdersForCustomer(String username) {
        User customer = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<Order> orders = orderRepository.findAllByCustomerId(customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .toList();
    }

    public PaymentIntent getPaymentIntent(String orderId) throws StripeException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        return PaymentIntent.retrieve(order.getPaymentIntentId());
    }

    public List<OrderResponseDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .toList();
    }

    public OrderResponseDto refundOrder(String orderId) throws StripeException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() != Order.Status.DELIVERED) {
            throw new IllegalStateException("Order is not eligible for refund");
        }

        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("payment_intent", order.getPaymentIntentId());
        com.stripe.model.Refund.create(refundParams);

        order.setStatus(Order.Status.REFUNDED);
        orderRepository.save(order);

        return mapToOrderResponseDto(order);
    }
}
