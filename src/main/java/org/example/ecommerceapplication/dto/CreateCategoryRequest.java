package org.example.ecommerceapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank(message = "Category Name is required")
    @Size(min = 3, max = 50, message = "Category Name must be 3-50 characters long")
    private String name;

    @NotBlank(message = "Category description is required")
    @Size(max = 500, message = "Category description cannot exceed 500 characters")
    private String description;
}
