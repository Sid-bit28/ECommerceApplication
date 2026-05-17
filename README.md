# 🛍️ E-Commerce Product Catalog

**Status:** Completed ✅

---

## 📋 Project Overview

**Product Catalog API** for an e-commerce platform with advanced features like **input validation**, **pagination**, **filtering**, and **DTO mapping**.

A REST API that manages an e-commerce product catalog with:

- ✅ Multiple products with categories
- ✅ Input validation at the API boundary
- ✅ Advanced filtering (by name, price range, category, status)
- ✅ Pagination and sorting capabilities
- ✅ Professional error handling and responses
- ✅ Clean DTO pattern for data transfer

### Real-World Scenario

Imagine building the backend for an e-commerce website.

- Allows store managers to add/update products with proper validation
- Provides customers with a searchable product catalog
- Supports filtering by category, price range, and availability
- Handles large product lists with pagination
- Returns helpful error messages when validation fails

---

## 🏗️ Project Architecture

```
ProductCatalogApplication
│
├── Controller Layer (REST Endpoints)
│   ├── ProductController
│   │   ├── GET    /api/products
│   │   ├── GET    /api/products/{id}
│   │   ├── POST   /api/products
│   │   ├── PUT    /api/products/{id}
│   │   └── DELETE /api/products/{id}
│   │
│   └── CategoryController
│       ├── GET    /api/categories
│       ├── GET    /api/categories/{id}
│       ├── POST   /api/categories
│       ├── PUT    /api/categories/{id}
│       └── DELETE /api/categories/{id}
│
├── Service Layer (Business Logic)
│   ├── ProductService
│   │   ├── getAllProducts(filter, sort, page)
│   │   ├── getProductById(id)
│   │   ├── createProduct(request)
│   │   ├── updateProduct(id, request)
│   │   ├── deleteProduct(id)
│   │   └── searchProducts(criteria)
│   │
│   └── CategoryService 
│       ├── getAllCategories()
│       ├── getCategoryById(id)
│       ├── createCategory(...)
│       ├── updateCategory(...)
│       └── deleteCategory(id)
│
├── Model/Domain Layer
│   ├── Product (id, name, description, price, quantity, category, active, timestamps)
│   └── Category (id, name, description, active, timestamps)
│
├── DTO Layer
│   ├── CreateProductRequest (with validation annotations)
│   ├── UpdateProductRequest (with validation annotations)
│   ├── ProductDTO (response)
│   ├── CreateCategoryRequest (with validation annotations)
│   ├── UpdateCategoryRequest (to build)
│   └── CategoryDTO (response)
│
└── Exception Layer
    ├── ResourceNotFoundException
    ├── ValidationErrorException (to build)
    ├── ErrorResponse
    └── GlobalExceptionHandler
```

---

### Priority 1: Core Implementation

1. **ProductService** - Full business logic
    - getAllProducts() with filtering
    - getProductById()
    - createProduct()
    - updateProduct()
    - deleteProduct()

2. **ProductController** - REST endpoints
    - Map all HTTP methods
    - Use @Valid for request validation
    - Implement filtering and pagination

3. **CategoryController** - REST endpoints
    - Create, read, update, delete categories
    - Proper error handling

### Priority 2: Enhancements

4. Update GlobalExceptionHandler to properly handle validation errors
5. Add logging throughout
6. Add request/response examples in documentation

---

### Products

```
GET    /api/products
       Filters: ?name=iPhone&categoryId=1&minPrice=50000&maxPrice=100000&active=true
       Pagination: ?page=0&size=10
       Response: Page<ProductDTO>

GET    /api/products/{id}
       Response: ProductDTO

POST   /api/products
       Body: CreateProductRequest
       Response: ProductDTO (201)

PUT    /api/products/{id}
       Body: UpdateProductRequest
       Response: ProductDTO

DELETE /api/products/{id}
       Response: 204 No Content
```

### Categories

```
GET    /api/categories
       Response: List<CategoryDTO>

GET    /api/categories/{id}
       Response: CategoryDTO

POST   /api/categories
       Body: CreateCategoryRequest
       Response: CategoryDTO (201)

PUT    /api/categories/{id}
       Body: UpdateCategoryRequest
       Response: CategoryDTO

DELETE /api/categories/{id}
       Response: 204 No Content
```

---


```
External Request (JSON)
    ↓ (deserialize)
CreateProductRequest (with @Valid annotations)
    ↓ (validate)
ProductService.createProduct(request)
    ↓ (map to entity)
Product (domain model)
    ↓ (save to storage)
Product (retrieved from storage)
    ↓ (map to DTO)
ProductDTO (response)
    ↓ (serialize)
External Response (JSON)
```

## 🧪 Testing Guide

### Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

### Test Creating a Category

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and gadgets"
  }'

# Response: 201 Created
{
    "id": 1,
    "name": "Electronics",
    "description": "Electronic devices and gadgets",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

### Test Creating a Product (with validation)

```bash
# ✅ Valid request
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest flagship smartphone with advanced features",
    "price": 79999.00,
    "quantity": 50,
    "categoryId": 1
  }'

# ❌ Invalid request (short name)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "X",  # Too short!
    "description": "Latest flagship smartphone with advanced features",
    "price": 79999.00,
    "quantity": 50,
    "categoryId": 1
  }'

# Response: 400 Bad Request
{
    "status": 400,
    "message": "Validation failed",
    "errors": {
        "name": "Product name must be 3-100 characters"
    }
}
```

### Test Filtering Products

```bash
# Get all Electronics products between ₹50k-₹100k
curl "http://localhost:8080/api/products?categoryId=1&minPrice=50000&maxPrice=100000&active=true&page=0&size=10"
```