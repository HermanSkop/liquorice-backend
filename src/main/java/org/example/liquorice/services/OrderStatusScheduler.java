package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.liquorice.config.BootstrapData;
import org.example.liquorice.models.Order;
import org.example.liquorice.repositories.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {
    private final OrderRepository orderRepository;

    @Scheduled(fixedDelay = 6000)
    public void updateOrderStatus() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> order.setStatus(getNextStatus(order.getStatus())));
        orderRepository.saveAll(orders);
        log.info("Order status updated");

    }

    private Order.Status getNextStatus(Order.Status currentStatus) {
        return switch (currentStatus) {
            case PROCESSING -> Order.Status.SHIPPING;
            case SHIPPING -> Order.Status.DELIVERED;
            default -> currentStatus;
        };
    }
}
