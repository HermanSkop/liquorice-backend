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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    @Transactional
    public Optional<OrderResponseDto> submitOrder(String customerEmail, OrderRequestDto orderRequest) throws StripeException {
        Optional<User> customerOpt = userRepository.findByEmail(customerEmail);
        if (customerOpt.isEmpty()) {
            return Optional.empty();
        }
        User customer = customerOpt.get();

        Optional<Order> orderOpt = orderRepository.findById(orderRequest.getOrderId());
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        Order order = orderOpt.get();

        if (!Objects.equals(order.getCustomerId(), customer.getId()))
            throw new IllegalArgumentException("Customer does not own this order");

        PaymentIntent paymentIntent = PaymentIntent.retrieve(order.getPaymentIntentId());
        String paymentStatus = paymentIntent.getStatus();
        if (!"succeeded".equals(paymentStatus))
            throw new IllegalStateException("Payment not completed. Current status: " + paymentStatus);

        order.setStatus(Order.Status.PROCESSING);

        return Optional.of(mapToOrderResponseDto(orderRepository.save(order)));
    }

    public Optional<Order> createOrder(String customerEmail, CartResponseDto cart, String paymentIntentId, AddressDto address) {
        Optional<User> customerOpt = userRepository.findByEmail(customerEmail);
        if (customerOpt.isEmpty()) {
            return Optional.empty();
        }
        User customer = customerOpt.get();

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

        return Optional.of(orderRepository.save(order));
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

    @Transactional
    public Optional<List<OrderResponseDto>> getOrdersForCustomer(String username) {
        Optional<User> customerOpt = userRepository.findByEmail(username);
        if (customerOpt.isEmpty()) {
            return Optional.empty();
        }
        User customer = customerOpt.get();

        Optional<List<Order>> ordersOpt = orderRepository.findAllByCustomerId(customer.getId());
        if (ordersOpt.isEmpty()) {
            return Optional.of(List.of());
        }

        List<OrderResponseDto> orderDtos = ordersOpt.get().stream()
                .map(this::mapToOrderResponseDto)
                .toList();

        return Optional.of(orderDtos);
    }

    @Transactional
    public Optional<List<OrderResponseDto>> getOrdersForCustomerById(String id) {
        Optional<User> customerOpt = userRepository.findById(id);
        if (customerOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<List<Order>> ordersOpt = orderRepository.findAllByCustomerId(id);
        if (ordersOpt.isEmpty()) {
            return Optional.of(List.of());
        }

        List<OrderResponseDto> orderDtos = ordersOpt.get().stream()
                .map(this::mapToOrderResponseDto)
                .toList();

        return Optional.of(orderDtos);
    }

    public Optional<PaymentIntent> getPaymentIntent(String orderId) throws StripeException {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(PaymentIntent.retrieve(orderOpt.get().getPaymentIntentId()));
    }

    public List<OrderResponseDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .toList();
    }

    @Transactional
    public Optional<OrderResponseDto> refundOrder(String orderId) throws StripeException {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        Order order = orderOpt.get();

        if (order.getStatus() != Order.Status.DELIVERED) {
            throw new IllegalStateException("Order is not eligible for refund");
        }

        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("payment_intent", order.getPaymentIntentId());
        com.stripe.model.Refund.create(refundParams);

        order.setStatus(Order.Status.REFUNDED);
        orderRepository.save(order);

        return Optional.of(mapToOrderResponseDto(order));
    }
}
