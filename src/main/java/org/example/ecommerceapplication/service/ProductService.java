package org.example.ecommerceapplication.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceapplication.model.Category;
import org.example.ecommerceapplication.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ProductService {
    private Long productIdCounter = 1L;
    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();

    private final CategoryService categoryService;

    public ProductService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Product createProduct(String name, String description, Double price, Integer quantity, Long categoryId) {
        log.info("Creating a new product with name: {}, description: {}, price: {}, quantity: {}", name, description, price, quantity);
        Category category = categoryService.getCategoryById(categoryId);

        if(category == null){
            log.error("Category with id: {} not found", categoryId);
            return null;
        }

        Product product = Product.builder()
                .id(productIdCounter++)
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
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


    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        List<Product> allProducts = new ArrayList<>();
        allProducts = productStore.values()
                .stream()
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                .toList();
        return allProducts;
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
    public Product updateProduct(Long id, String name, String description, Double price, Integer quantity, Long CategoryId, Boolean active) {
        log.info("Updating product with name: {}, description: {}, price: {}, quantity: {}", name, description, price, quantity);
        Product product = productStore.get(id);
        if (product == null) {
            throw new RuntimeException("Product with id " + id + " not found");
        }
        if(CategoryId != null) {
            Category category = categoryService.getCategoryById(CategoryId);
            if(category == null){
                throw new RuntimeException("Category with id " + id + " not found");
            }
            product.setCategory(category);
        }
        if(name != null && !name.trim().isEmpty()) {
            product.setName(name);
        }
        if(description != null && !description.trim().isEmpty()) {
            product.setDescription(description);
        }
        if(price != null) {
            product.setPrice(price);
        }
        if(quantity != null) {
            product.setQuantity(quantity);
        }
        if(active != null) {
            product.setActive(active);
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
