package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.models.User;
import org.example.liquorice.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> registerCustomer(String email, String password) {
        if(userRepository.existsByEmail(email)) {
            return Optional.empty();
        }

        User customer = User.builder()
                .email(email)
                .role(User.Role.CUSTOMER)
                .password(passwordEncoder.encode(password))
                .build();

        return Optional.of(userRepository.save(customer));
    }
}
