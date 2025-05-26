package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.model.*;
import ed.lab.ed1labo04.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CreateCartRequest request) {
        try {
            CartResponse cart = cartService.createCart(request);
            return ResponseEntity.status(201).body(cart);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cartService.getCart(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
