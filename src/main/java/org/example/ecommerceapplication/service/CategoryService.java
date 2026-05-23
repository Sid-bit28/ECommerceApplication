package org.example.ecommerceapplication.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceapplication.dto.CreateCategoryRequest;
import org.example.ecommerceapplication.dto.UpdateCategoryRequest;
import org.example.ecommerceapplication.exception.ResourceNotFoundException;
import org.example.ecommerceapplication.model.Category;
import org.example.ecommerceapplication.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
@Slf4j
public class CategoryService {
    private Long categoryIdCounter = 1L;
    private final Map<Long, Category> categoryStore = new ConcurrentHashMap<>();

    public Page<Category> getAllCategories(String name, Pageable pageable) {
        log.info("Getting all categories");
        Stream<Category> stream = categoryStore.values().stream();
        if(name != null && !name.trim().isEmpty()) {
            stream = stream.filter(category -> category.getName().toLowerCase().contains(name.toLowerCase()));
        }

        List<Category> filteredCategories = stream.sorted(Comparator.comparing(Category::getCreatedAt).reversed()).toList();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredCategories.size());
        List<Category> pageContent = filteredCategories.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filteredCategories.size());
    }

    public Category getCategoryById(Long id) {
        log.info("Getting category by id {}", id);
        return categoryStore.values()
                .stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    public Category createCategory(CreateCategoryRequest request) {
        log.info("Creating new category with name: {}, description: {}", request.getName(), request.getDescription());
        Category category = Category.builder()
                .id(categoryIdCounter++)
                .name(request.getName())
                .description(request.getDescription())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        categoryStore.put(category.getId(), category);
        log.info("Created category with id: {}", category.getId());
        return category;
    }

    public Category updateCategory(Long id, UpdateCategoryRequest request) {
        log.info("Updating category with id: {}, name: {}, description: {}", id, request.getName(), request.getDescription());
        Category category = categoryStore.get(id);
        if (category == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        if(request.getName() != null && !request.getName().isEmpty()) {
            category.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().isEmpty()) {
            category.setDescription(request.getDescription());
        }
        category.setUpdatedAt(LocalDateTime.now());
        categoryStore.put(category.getId(), category);
        log.info("Updated category with id: {}", category.getId());
        return category;
    }

    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        if(!categoryStore.containsKey(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        categoryStore.remove(id);
        log.info("Deleted category with id: {}", id);
    }
}
