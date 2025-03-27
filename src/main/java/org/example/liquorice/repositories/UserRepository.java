package org.example.liquorice.repositories;

import jakarta.validation.constraints.Email;
import org.example.liquorice.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(@Email String email);

    boolean existsByEmail(String email);
}
