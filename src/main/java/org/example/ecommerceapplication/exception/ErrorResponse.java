package org.example.ecommerceapplication.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
Standard format for all error responses.

Includes:
- timestamp: When error occurred
- status: HTTP status code
- error : Error Type
- message: Error description
- validationErrors: For validation failures
- path: Which endpoint caused this error

Example for validation error:
{
    "timestamp": "2025-05-16T13:48:00",
    "status": 400,
    "error": "VALIDATION_ERROR",
    "message": "Validation failed",
    "validationErrors": [
    {
        "field": "name",
        "message": "Product name must be 3-100 characters"
    },
    {
        "field": "price",
        "message": "Price must be at least ₹0.01"
    }
    ],
    "path": "/api/products"
}
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<FieldError> validationErrors;
    private String path;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
