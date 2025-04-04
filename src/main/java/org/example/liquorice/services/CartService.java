package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.dtos.CartItemDto;
import org.example.liquorice.dtos.CartRequestDto;
import org.example.liquorice.dtos.CartResponseDto;
import org.example.liquorice.dtos.ProductPreviewDto;
import org.example.liquorice.models.Cart;
import org.example.liquorice.models.Product;
import org.example.liquorice.models.User;
import org.example.liquorice.repositories.CartRepository;
import org.example.liquorice.repositories.ProductRepository;
import org.example.liquorice.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Transactional
    public Optional<CartResponseDto> getCart(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        String userId = userOptional.get().getId();
        Cart cart = cartRepository.findById(userId).orElse(new Cart());

        List<CartItemDto> cartItems = cart.getProductQuantities().keySet()
                .stream()
                .map(productId -> productRepository.findById(productId).orElse(null))
                .filter(Objects::nonNull)
                .map(product -> new CartItemDto(
                                productService.mapToProductPreviewDto(product),
                                cart.getProductQuantities().get(product.getId()))
                )
                .collect(Collectors.toList());

        return Optional.of(new CartResponseDto(cartItems));
    }

    @Transactional
    public Optional<CartResponseDto> saveCart(CartRequestDto cart, String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        String userId = userOptional.get().getId();

        List<Product> products = fetchProducts(cart.getProductQuantities());
        products.forEach(product -> {
            if (product.getAmountLeft() < cart.getProductQuantities().get(product.getId())) {
                throw new IllegalArgumentException("Not enough product available");
            } else if (!product.isAvailable()) {
                throw new IllegalArgumentException("Product is not available");
            }
        });

        Cart existingCart = new Cart();
        existingCart.setUserId(userId);
        existingCart.setProductQuantities(cart.getProductQuantities());
        cartRepository.save(existingCart);

        return getCart(userEmail);
    }

    private List<Product> fetchProducts(Map<String, Integer> productQuantities) {
        return productRepository.findAllById(productQuantities.keySet());
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
