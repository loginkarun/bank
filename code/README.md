# MyProject - Shopping Cart SpringBoot Application

## Overview
A complete SpringBoot 3.5.9 application implementing shopping cart functionality with JDK 21.

## Features
- Add items to cart
- View cart contents
- Remove items from cart
- Update item quantities
- Real-time cart total calculation
- Product stock validation
- Comprehensive error handling

## Technology Stack
- **Java**: JDK 21
- **Spring Boot**: 3.5.9
- **Database**: H2 (In-Memory)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Code Coverage**: JaCoCo
- **API Documentation**: SpringDoc OpenAPI (Swagger)

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/       # REST API Controllers
│   │   │   ├── models/
│   │   │   │   ├── dtos/         # Data Transfer Objects
│   │   │   │   ├── entities/     # JPA Entities
│   │   │   │   └── repositories/ # JPA Repositories
│   │   │   ├── services/
│   │   │   │   ├── impl/         # Service Implementations
│   │   │   │   └── interfaces/   # Service Interfaces
│   │   │   ├── config/           # Configuration Classes
│   │   │   ├── exceptions/       # Custom Exceptions
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Unit and Integration Tests
└── pom.xml
```

## API Endpoints

### 1. Add Item to Cart
**POST** `/api/cart/add`
```json
{
  "productId": 1,
  "quantity": 2
}
```

### 2. Get Cart
**GET** `/api/cart`

### 3. Remove Item from Cart
**DELETE** `/api/cart/item`
```json
{
  "productId": 1
}
```

### 4. Update Item Quantity
**PUT** `/api/cart/item`
```json
{
  "productId": 1,
  "quantity": 5
}
```

## Running the Application

### Prerequisites
- JDK 21 installed
- Maven 3.6+ installed

### Build and Run
```bash
cd code
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing

### Run Tests
```bash
cd code
mvn test
```

### Generate Coverage Report
```bash
cd code
mvn jacoco:report
```
Coverage report will be available at: `target/site/jacoco/index.html`

## API Documentation

Once the application is running, access:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/api-docs

## Database Console

H2 Console is available at: http://localhost:8080/api/h2-console
- **JDBC URL**: jdbc:h2:mem:cartdb
- **Username**: sa
- **Password**: (leave empty)

## Sample Products

The application initializes with sample products:
1. Laptop - $999.99 (Stock: 10)
2. Mouse - $29.99 (Stock: 50)
3. Keyboard - $79.99 (Stock: 30)
4. Monitor - $299.99 (Stock: 15)
5. Headphones - $149.99 (Stock: 25)

## Error Handling

The application provides structured error responses:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "traceId": "abc-123-def",
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product not found",
  "details": [
    {
      "field": "productId",
      "issue": "Product does not exist in database"
    }
  ]
}
```

## CORS Configuration

CORS is enabled for Angular frontend integration:
- **Allowed Origin**: http://localhost:4200
- **Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS, PATCH
- **Allowed Headers**: All
- **Credentials**: Enabled

## Security

The application uses Spring Security with:
- JWT token-based authentication (configured but permissive for demo)
- All API endpoints are accessible for testing
- Production deployment should enable proper authentication

## GitHub Actions Workflow

Automated build and test workflow is configured:
- Builds on push to main branch
- Runs all tests
- Generates JaCoCo coverage report
- Uploads test results and coverage artifacts

## LLD Reference

This implementation is based on:
- **JIRA Issue**: SCRUM-11692
- **Feature**: Shopping Cart Add Item Feature
- **LLD Document**: lld/lld_SCRUM-11692.md

## License

This project is part of the bank repository and follows its licensing terms.