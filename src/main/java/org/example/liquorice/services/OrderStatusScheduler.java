package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.BootstrapData;
import org.example.liquorice.models.Order;
import org.example.liquorice.repositories.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {
    private final OrderRepository orderRepository;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BootstrapData.class);


    @Scheduled(fixedDelay = 6000)
    public void updateOrderStatus() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> order.setStatus(getNextStatus(order.getStatus())));
        orderRepository.saveAll(orders);
        logger.info("Order status updated");

    }

    private Order.Status getNextStatus(Order.Status currentStatus) {
        return switch (currentStatus) {
            case PROCESSING -> Order.Status.SHIPPING;
            case SHIPPING -> Order.Status.DELIVERED;
            default -> currentStatus;
        };
    }
}
