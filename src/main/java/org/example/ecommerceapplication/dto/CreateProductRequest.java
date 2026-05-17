package org.example.ecommerceapplication.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Name must be 3-100 characters long")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 1000, message = "Description must be 10-1000 characters long")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least $0.01")
    @DecimalMax(value = "10000000", message = "Price connect exceed $10000000")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Category ID is a must")
    @Positive(message = "Category ID must be valid")
    private Long categoryId;
}
