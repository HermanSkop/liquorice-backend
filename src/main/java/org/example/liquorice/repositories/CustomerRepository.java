package org.example.liquorice.repositories;

import org.example.liquorice.models.user.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByEmail(String customerEmail);
}
