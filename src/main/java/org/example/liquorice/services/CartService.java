package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.dtos.CartItemDto;
import org.example.liquorice.dtos.CartRequestDto;
import org.example.liquorice.dtos.CartResponseDto;
import org.example.liquorice.dtos.ProductPreviewDto;
import org.example.liquorice.models.Cart;
import org.example.liquorice.models.user.User;
import org.example.liquorice.repositories.CartRepository;
import org.example.liquorice.repositories.ProductRepository;
import org.example.liquorice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public CartResponseDto getCart(String userEmail) {
        String userId = userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findById(userId).orElse(new Cart());

        return new CartResponseDto(cart.getProductQuantities().keySet()
                .stream()
                .map(productId -> productRepository.findById(productId).orElse(null))
                .filter(Objects::nonNull)
                .map(product -> new CartItemDto(
                                productService.mapToProductPreviewDto(product),
                                cart.getProductQuantities().get(product.getId()))
                )
                .collect(Collectors.toList()));
    }


    public void saveCart(CartRequestDto cart, String userEmail) {
        String userId = userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart existingCart = cartRepository.findById(userId).orElse(new Cart());
        existingCart.setUserId(userId);
        existingCart.setProductQuantities(cart.getProductQuantities());
        cartRepository.save(existingCart);
    }

    public double getTotalPrice(CartResponseDto cart) {
        return cart.getCartItems()
                .stream()
                .mapToDouble(cartItem -> {
                    ProductPreviewDto product = cartItem.getProduct();
                    return product.getPrice() * cartItem.getQuantity();
                })
                .sum();
    }
}