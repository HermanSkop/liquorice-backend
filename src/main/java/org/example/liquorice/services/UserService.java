package org.example.liquorice.services;

import org.example.liquorice.models.user.Customer;
import org.example.liquorice.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerCustomer(String email, String password) {
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        Customer customer = Customer.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(customer);
    }
}
