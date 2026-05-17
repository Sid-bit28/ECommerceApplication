package org.example.ecommerceapplication.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequest {
    @Size(min = 3, max = 50, message = "Category Name must be 3-50 characters long")
    private String name;

    @Size(max = 500, message = "Category description cannot exceed 500 characters")
    private String description;

}
