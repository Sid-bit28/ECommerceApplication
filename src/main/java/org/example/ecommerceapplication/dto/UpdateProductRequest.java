package org.example.ecommerceapplication.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    @Size(min = 3, max = 100, message = "Name must be between 3-100 characters long")
    private String name;

    @Size(min = 10, max = 1000, message = "Description must be 10-1000 characters long")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be at least $0.01")
    @DecimalMax(value = "1000000", message = "Price cannot exceed $1000000")
    private Double price;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @Positive(message = "CategoryId must be valid")
    private Long CategoryId;

    private Boolean active;
}
