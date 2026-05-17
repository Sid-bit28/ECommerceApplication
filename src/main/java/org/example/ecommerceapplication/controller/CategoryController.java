package org.example.ecommerceapplication.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceapplication.dto.CategoryDTO;
import org.example.ecommerceapplication.dto.CreateCategoryRequest;
import org.example.ecommerceapplication.dto.UpdateCategoryRequest;
import org.example.ecommerceapplication.model.Category;
import org.example.ecommerceapplication.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        log.info("GET /api/category fetching all categories with filters: name: {}, page: {}, size: {}", name, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryService.getAllCategories(name, pageable);
        Page<CategoryDTO> dtos = categories.map(this::mapCategoryToDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/category/{} Getting category by id: {}", id, id);
        Category category = categoryService.getCategoryById(id);
        CategoryDTO dto = mapCategoryToDto(category);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,@Valid @RequestBody UpdateCategoryRequest request) {
        log.info("PUT /api/category/{} Updating category with id: {}, request: {}", id, id, request.toString());
        Category category = categoryService.updateCategory(id, request);
        CategoryDTO dto = mapCategoryToDto(category);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("POST /api/category Adding category: {}", request.toString());
        Category category = categoryService.createCategory(request);
        CategoryDTO dto = mapCategoryToDto(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/category/{} Deleting category: {}", id, id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDTO mapCategoryToDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
