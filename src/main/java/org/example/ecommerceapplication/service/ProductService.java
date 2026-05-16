package org.example.ecommerceapplication.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceapplication.dto.CreateProductRequest;
import org.example.ecommerceapplication.dto.UpdateProductRequest;
import org.example.ecommerceapplication.model.Category;
import org.example.ecommerceapplication.model.Product;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProductService {
    private Long productIdCounter = 1L;
    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();

    private final CategoryService categoryService;

    public ProductService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Product createProduct(CreateProductRequest request) {
        log.info("Creating a new product with name: {}, description: {}, price: {}, quantity: {}", request.getName(), request.getDescription(), request.getPrice(), request.getQuantity());
        Category category = categoryService.getCategoryById(request.getCategoryId());

        if(category == null){
            throw new RuntimeException("Category not found");
        }

        Product product = Product.builder()
                .id(productIdCounter++)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(category)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Created product with id: {}", product.getId());
        productStore.put(product.getId(), product);
        product.setCategory(category);
        return product;
    }


    public Page<Product> getAllProducts(String name, Long categoryId, Double minPrice, Double maxPrice, Boolean active, Pageable pageable) {
        Stream<Product> stream = productStore.values().stream();

        // Applying filters
        if(name != null && !name.trim().isEmpty()) {
            stream = stream.filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()));
        }

        if(categoryId != null) {
            stream = stream.filter(product -> product.getCategory().getId().equals(categoryId));
        }

        if(minPrice != null) {
            stream = stream.filter(product -> product.getPrice() >= minPrice);
        }

        if(maxPrice != null) {
            stream = stream.filter(product -> product.getPrice() <= maxPrice);
        }

        if(active != null) {
            stream = stream.filter(product -> product.getActive() == active);
        }

        List<Product> filteredProducts = stream.sorted(Comparator.comparing(Product::getCreatedAt).reversed()).toList();

        // Pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredProducts.size());
        List<Product> pageContent = filteredProducts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filteredProducts.size());
    }

    // :TODO Create a ResourceNotFoundExceptionHandler
    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productStore.get(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    // :TODO Create a ResourceNotFoundExceptionHandler
    public Product updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with name: {}, description: {}, price: {}, quantity: {}", request.getName(), request.getDescription(), request.getPrice(), request.getQuantity());
        Product product = productStore.get(id);
        if (product == null) {
            throw new RuntimeException("Product with id " + id + " not found");
        }
        if(request.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(request.getCategoryId());
            if(category == null){
                throw new RuntimeException("Category with id " + id + " not found");
            }
            product.setCategory(category);
        }
        if(request.getName() != null && !request.getName().trim().isEmpty()) {
            product.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            product.setDescription(request.getDescription());
        }
        if(request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if(request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }
        if(request.getActive() != null) {
            product.setActive(request.getActive());
        }
        product.setUpdatedAt(LocalDateTime.now());
        log.info("Updated product with id: {}", product.getId());
        return product;
    }

    // :TODO Create a ResourceNotFoundExceptionHandler
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        if(!productStore.containsKey(id)){
            throw new RuntimeException("Product with id " + id + " not found");
        }
        productStore.remove(id);
        log.info("Deleted product with id: {}", id);
    }
}
