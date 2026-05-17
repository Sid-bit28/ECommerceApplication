package org.example.ecommerceapplication.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceapplication.dto.CategoryDTO;
import org.example.ecommerceapplication.dto.CreateProductRequest;
import org.example.ecommerceapplication.dto.ProductDTO;
import org.example.ecommerceapplication.dto.UpdateProductRequest;
import org.example.ecommerceapplication.model.Category;
import org.example.ecommerceapplication.model.Product;
import org.example.ecommerceapplication.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /api/products - Filters: name = {}, categoryId = {}, price = {} - {}, active = {}", name, categoryId, minPrice, maxPrice, active);

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productService.getAllProducts(name, categoryId, minPrice, maxPrice, active, pageable);

        Page<ProductDTO> dtos = products.map(this::mapProductToDto);
        log.info("Returning {} products on page {} of {}",
                dtos.getNumberOfElements(), page, dtos.getTotalPages());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.info("GET /api/products/{id} - Filters: id = {}", id);
        Product product = productService.getProductById(id);
        ProductDTO productDTO = mapProductToDto(product);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("POST /api/products - request: {}", request.toString());
        Product product = productService.createProduct(request);
        ProductDTO productDTO = mapProductToDto(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        log.info("PUT /api/products/{id} - id: {}, request: {}", id, request.toString());
        Product product = productService.updateProduct(id, request);
        ProductDTO productDTO = mapProductToDto(product);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{id} - id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private ProductDTO mapProductToDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory() == null ? null : mapCategoryToDto(product.getCategory()))
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private CategoryDTO mapCategoryToDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }
}
