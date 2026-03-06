# Shopping Cart Management System

## Overview
This is a Spring Boot 3.5.9 application built with JDK 21 that implements a shopping cart management system. The application provides RESTful APIs for managing shopping cart operations including adding items, updating quantities, removing items, and clearing the cart.

## Features
- Add products to shopping cart
- Update item quantities
- Remove items from cart
- Clear entire cart
- View cart contents with totals
- Product stock validation
- Real-time cart total calculation
- Session-based cart persistence

## Technology Stack
- **Java**: JDK 21
- **Framework**: Spring Boot 3.5.9
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Code Coverage**: JaCoCo

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/       # REST Controllers
│   │   │   ├── services/          # Business Logic
│   │   │   │   ├── interfaces/
│   │   │   │   └── impl/
│   │   │   ├── models/
│   │   │   │   ├── entities/      # JPA Entities
│   │   │   │   ├── dtos/          # Data Transfer Objects
│   │   │   │   └── repositories/  # JPA Repositories
│   │   │   ├── config/            # Configuration Classes
│   │   │   ├── exceptions/        # Custom Exceptions
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                      # Unit and Integration Tests
└── pom.xml
```

## API Endpoints

### Cart Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cart/add` | Add item to cart |
| GET | `/api/cart` | Get cart contents |
| PUT | `/api/cart/item/{productId}` | Update item quantity |
| DELETE | `/api/cart/item/{productId}` | Remove item from cart |
| DELETE | `/api/cart` | Clear entire cart |

## Getting Started

### Prerequisites
- JDK 21
- Maven 3.6+

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/loginkarun/bank.git
cd bank/code
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### Running Tests

```bash
mvn test
```

### Generate Coverage Report

```bash
mvn jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/api/swagger-ui.html
```

OpenAPI specification available at:
```
http://localhost:8080/api/api-docs
```

## Database Console

H2 Console is available at:
```
http://localhost:8080/api/h2-console
```

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Sample Products

The application initializes with the following sample products:
1. Wireless Mouse - $29.99 (50 in stock)
2. Mechanical Keyboard - $89.99 (30 in stock)
3. USB-C Hub - $45.50 (25 in stock)
4. Laptop Stand - $35.00 (40 in stock)
5. Webcam HD - $79.99 (20 in stock)

## Testing the API

### Add Item to Cart
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

### Get Cart
```bash
curl -X GET http://localhost:8080/api/cart \
  -H "X-User-Id: 1"
```

### Update Quantity
```bash
curl -X PUT http://localhost:8080/api/cart/item/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{
    "quantity": 3
  }'
```

### Remove Item
```bash
curl -X DELETE http://localhost:8080/api/cart/item/1 \
  -H "X-User-Id: 1"
```

### Clear Cart
```bash
curl -X DELETE http://localhost:8080/api/cart \
  -H "X-User-Id: 1"
```

## CORS Configuration

CORS is configured to allow requests from:
- `http://localhost:4200` (Angular development server)

## Security

- Session-based authentication (simplified for demo)
- User ID passed via `X-User-Id` header
- CSRF disabled for REST API
- H2 console accessible in development mode

## Error Handling

The application provides structured error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "traceId": "abc123-def456",
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product not found with ID: 999",
  "details": []
}
```

## Validation Rules

- Product ID: Must exist in database
- Quantity: Must be >= 1 and <= available stock
- User ID: Required for all cart operations

## CI/CD

GitHub Actions workflow is configured for:
- Building the application
- Running tests
- Generating coverage reports
- Uploading artifacts

Workflow file: `.github/workflows/build.yml`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please open an issue in the GitHub repository.
