package ed.lab.ed1labo04.service;

import ed.lab.ed1labo04.entity.*;
import ed.lab.ed1labo04.model.*;
import ed.lab.ed1labo04.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartResponse createCart(CreateCartRequest request) {
        List<CartItemEntity> cartItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItemRequest itemRequest : request.getCartItems()) {
            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient inventory for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            CartItemEntity item = new CartItemEntity();
            item.setProductId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemRequest.getQuantity());

            cartItems.add(item);
            totalPrice += product.getPrice() * itemRequest.getQuantity();
        }

        CartEntity cart = new CartEntity();
        cart.setTotalPrice(totalPrice);
        cart.setCartItems(cartItems);
        cartItems.forEach(item -> item.setCart(cart));

        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    public CartResponse getCart(Long id) {
        CartEntity cart = cartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        return toCartResponse(cart);
    }

    private CartResponse toCartResponse(CartEntity cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream().map(item -> {
            CartItemResponse response = new CartItemResponse();
            response.setProductId(item.getProductId());
            response.setName(item.getName());
            response.setPrice(item.getPrice());
            response.setQuantity(item.getQuantity());
            return response;
        }).collect(Collectors.toList());

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setCartItems(itemResponses);
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }
}

