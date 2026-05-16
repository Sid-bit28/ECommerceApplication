package org.example.ecommerceapplication.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceapplication.model.Category;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CategoryService {
    private Long categoryIdCounter = 1L;
    private final Map<Long, Category> categoryStore = new ConcurrentHashMap<>();

    public List<Category> getAllCategories() {
        log.info("Getting all categories");
        List<Category> categories = new ArrayList<>();
        categories = categoryStore
                .values()
                .stream()
                .sorted(Comparator.comparing(Category::getCreatedAt).reversed())
                .toList();
        return categories;
    }

    // :Todo Create a custom CategoryNotFoundException in GlobalHandler
    public Category getCategoryById(Long id) {
        log.info("Getting category by id {}", id);
        return categoryStore.values()
                .stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
    }

    public Category createCategory(String name, String description) {
        log.info("Creating new category with name: {}, description: {}", name, description);
        Category category = Category.builder()
                .id(categoryIdCounter++)
                .name(name)
                .description(description)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        categoryStore.put(category.getId(), category);
        log.info("Created category with id: {}", category.getId());
        return category;
    }

    public Category updateCategory(Long id, String name, String description) {
        log.info("Updating category with id: {}, name: {}, description: {}", id, name, description);
        Category category = categoryStore.get(id);
        if(name != null && !name.isEmpty()) {
            category.setName(name);
        }
        if(description != null && !description.isEmpty()) {
            category.setDescription(description);
        }
        category.setUpdatedAt(LocalDateTime.now());
        categoryStore.put(category.getId(), category);
        log.info("Updated category with id: {}", category.getId());
        return category;
    }

    // :TODO Create a custom ResourceNotFoundException handled by GlobalExceptionHandler
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        if(!categoryStore.containsKey(id)) {
            throw new RuntimeException("Category with id " + id + " not found");
        }
        categoryStore.remove(id);
        log.info("Deleted category with id: {}", id);
    }
}
