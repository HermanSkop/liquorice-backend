package org.example.liquorice.repositories;

import org.example.liquorice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<List<Order>> findAllByCustomerId(String customerId);
}
