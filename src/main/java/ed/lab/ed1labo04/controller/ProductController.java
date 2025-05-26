package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CreateProductRequest;
import ed.lab.ed1labo04.model.UpdateProductRequest;
import ed.lab.ed1labo04.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        try {
            ProductEntity product = productService.createProduct(request);
            return ResponseEntity.status(201).body(product);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        try {
            ProductEntity product = productService.updateProduct(id, request);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
