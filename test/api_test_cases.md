# Shopping Cart API Test Cases

## Test Suite Information
- **Application**: Shopping Cart Management System
- **Base URL**: http://localhost:8080/api
- **API Version**: 1.0.0
- **Test Environment**: Development
- **Date Generated**: 2024-01-15

---

## Test Case Summary

| Test Case ID | Endpoint | Scenario | Priority | Status |
|--------------|----------|----------|----------|--------|
| TC-001 | POST /cart/add | Add item to cart - Happy path | High | Ready |
| TC-002 | POST /cart/add | Add item - Invalid product ID | High | Ready |
| TC-003 | POST /cart/add | Add item - Invalid quantity (zero) | High | Ready |
| TC-004 | POST /cart/add | Add item - Out of stock | High | Ready |
| TC-005 | POST /cart/add | Add same item multiple times | High | Ready |
| TC-006 | GET /cart | Get cart contents - Happy path | High | Ready |
| TC-007 | GET /cart | Get empty cart | Medium | Ready |
| TC-008 | PUT /cart/item/{productId} | Update quantity - Happy path | High | Ready |
| TC-009 | PUT /cart/item/{productId} | Update quantity - Invalid quantity | High | Ready |
| TC-010 | PUT /cart/item/{productId} | Update quantity - Product not in cart | High | Ready |
| TC-011 | PUT /cart/item/{productId} | Update quantity - Exceeds stock | High | Ready |
| TC-012 | DELETE /cart/item/{productId} | Remove item - Happy path | High | Ready |
| TC-013 | DELETE /cart/item/{productId} | Remove item - Product not in cart | High | Ready |
| TC-014 | DELETE /cart | Clear cart - Happy path | High | Ready |
| TC-015 | DELETE /cart | Clear empty cart | Medium | Ready |

---

## Detailed Test Cases

### TC-001: Add Item to Cart - Happy Path

**Test Case ID**: TC-001  
**Priority**: High  
**Test Type**: Positive  
**Endpoint**: POST /api/cart/add

**Objective**: Verify that a valid product can be successfully added to the shopping cart.

**Preconditions**:
- Application is running on http://localhost:8080
- Database contains product with ID 1
- Product has sufficient stock (>= 2 units)
- User session is valid (userId = 1)

**Test Data**:
```json
{
  "productId": 1,
  "quantity": 2
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Test Steps**:
1. Send POST request to /api/cart/add with valid product ID and quantity
2. Verify response status code
3. Verify response body structure
4. Verify item is added to cart
5. Verify total price is calculated correctly
6. Verify response time

**Expected Result**:
- Status Code: 201 Created
- Response contains:
  - id (cart ID)
  - userId = 1
  - items array with added product
  - totalPrice > 0
  - itemCount >= 2
- Response time < 500ms
- Product quantity in cart = 2

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: This is the primary happy path test case.

---

### TC-002: Add Item - Invalid Product ID

**Test Case ID**: TC-002  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: POST /api/cart/add

**Objective**: Verify that the system returns appropriate error when attempting to add a non-existent product.

**Preconditions**:
- Application is running
- Product with ID 999 does not exist in database
- User session is valid

**Test Data**:
```json
{
  "productId": 999,
  "quantity": 1
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Test Steps**:
1. Send POST request with non-existent product ID
2. Verify error response status code
3. Verify error response structure
4. Verify error code and message

**Expected Result**:
- Status Code: 404 Not Found
- Error response contains:
  - timestamp
  - traceId
  - errorCode = "PRODUCT_NOT_FOUND"
  - message indicating product not found
  - details array

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests product existence validation.

---

### TC-003: Add Item - Invalid Quantity (Zero)

**Test Case ID**: TC-003  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: POST /api/cart/add

**Objective**: Verify that the system validates quantity and rejects zero or negative values.

**Preconditions**:
- Application is running
- Product with ID 1 exists
- User session is valid

**Test Data**:
```json
{
  "productId": 1,
  "quantity": 0
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Test Steps**:
1. Send POST request with quantity = 0
2. Verify validation error response
3. Verify error details contain field-level validation

**Expected Result**:
- Status Code: 400 Bad Request
- Error response contains:
  - errorCode = "VALIDATION_ERROR"
  - message indicating validation failure
  - details array with field "quantity" and issue "Quantity must be at least 1"

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests @Min validation annotation.

---

### TC-004: Add Item - Out of Stock

**Test Case ID**: TC-004  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: POST /api/cart/add

**Objective**: Verify that the system prevents adding more items than available in stock.

**Preconditions**:
- Application is running
- Product with ID 1 exists with stock < 1000
- User session is valid

**Test Data**:
```json
{
  "productId": 1,
  "quantity": 1000
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Test Steps**:
1. Send POST request with quantity exceeding available stock
2. Verify out of stock error response
3. Verify error message contains stock information

**Expected Result**:
- Status Code: 409 Conflict
- Error response contains:
  - errorCode = "OUT_OF_STOCK"
  - message indicating insufficient stock
  - details about requested vs available quantity

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests stock validation business logic.

---

### TC-005: Add Same Item Multiple Times

**Test Case ID**: TC-005  
**Priority**: High  
**Test Type**: Positive  
**Endpoint**: POST /api/cart/add

**Objective**: Verify that adding the same product multiple times increments the quantity correctly.

**Preconditions**:
- Application is running
- Product with ID 2 exists with sufficient stock
- User session is valid
- Cart may or may not already contain the product

**Test Data**:
```json
{
  "productId": 2,
  "quantity": 1
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Test Steps**:
1. Send POST request to add product (first time)
2. Note the quantity in response
3. Send POST request again with same product ID
4. Verify quantity is incremented
5. Verify total price is updated correctly

**Expected Result**:
- Status Code: 201 Created (both times)
- Second response shows:
  - Same product in items array
  - Quantity incremented by 1
  - Total price increased by product price
  - No duplicate items in cart

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests quantity increment logic for existing items.

---

### TC-006: Get Cart Contents - Happy Path

**Test Case ID**: TC-006  
**Priority**: High  
**Test Type**: Positive  
**Endpoint**: GET /api/cart

**Objective**: Verify that the system returns the current cart contents correctly.

**Preconditions**:
- Application is running
- User has items in cart (from previous add operations)
- User session is valid

**Test Data**: None (GET request)

**Headers**:
- X-User-Id: 1

**Test Steps**:
1. Send GET request to /api/cart
2. Verify response status code
3. Verify response structure
4. Verify all cart items are returned
5. Verify total price calculation
6. Verify response time

**Expected Result**:
- Status Code: 200 OK
- Response contains:
  - id (cart ID)
  - userId = 1
  - items array with all cart items
  - Each item has: id, productId, productName, quantity, price, subtotal
  - totalPrice = sum of all subtotals
  - itemCount = sum of all quantities
- Response time < 500ms

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Primary cart retrieval test.

---

### TC-007: Get Empty Cart

**Test Case ID**: TC-007  
**Priority**: Medium  
**Test Type**: Positive  
**Endpoint**: GET /api/cart

**Objective**: Verify that the system handles empty cart scenario correctly.

**Preconditions**:
- Application is running
- User has no items in cart (or cart has been cleared)
- User session is valid

**Test Data**: None

**Headers**:
- X-User-Id: 1

**Test Steps**:
1. Clear cart if needed (DELETE /api/cart)
2. Send GET request to /api/cart
3. Verify empty cart response

**Expected Result**:
- Status Code: 200 OK
- Response contains:
  - id (cart ID)
  - userId = 1
  - items = empty array []
  - totalPrice = 0.0
  - itemCount = 0

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests edge case of empty cart.

---

### TC-008: Update Quantity - Happy Path

**Test Case ID**: TC-008  
**Priority**: High  
**Test Type**: Positive  
**Endpoint**: PUT /api/cart/item/{productId}

**Objective**: Verify that item quantity can be successfully updated in the cart.

**Preconditions**:
- Application is running
- Cart contains product with ID 1
- New quantity is within available stock
- User session is valid

**Test Data**:
```json
{
  "quantity": 3
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Path Parameters**:
- productId: 1

**Test Steps**:
1. Ensure product is in cart
2. Send PUT request to /api/cart/item/1 with new quantity
3. Verify response status code
4. Verify quantity is updated
5. Verify total price is recalculated
6. Verify response time

**Expected Result**:
- Status Code: 200 OK
- Response contains:
  - Updated cart with new quantity for product
  - Recalculated totalPrice
  - Updated itemCount
- Response time < 500ms

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests quantity update functionality.

---

### TC-009: Update Quantity - Invalid Quantity

**Test Case ID**: TC-009  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: PUT /api/cart/item/{productId}

**Objective**: Verify that the system validates quantity during update and rejects invalid values.

**Preconditions**:
- Application is running
- Cart contains product with ID 1
- User session is valid

**Test Data**:
```json
{
  "quantity": 0
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Path Parameters**:
- productId: 1

**Test Steps**:
1. Send PUT request with invalid quantity (0)
2. Verify validation error response

**Expected Result**:
- Status Code: 400 Bad Request
- Error response contains:
  - errorCode = "VALIDATION_ERROR"
  - details with field "quantity" validation error

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests validation on update operation.

---

### TC-010: Update Quantity - Product Not in Cart

**Test Case ID**: TC-010  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: PUT /api/cart/item/{productId}

**Objective**: Verify that the system returns appropriate error when attempting to update a product not in cart.

**Preconditions**:
- Application is running
- Product with ID 999 is not in user's cart
- User session is valid

**Test Data**:
```json
{
  "quantity": 2
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Path Parameters**:
- productId: 999

**Test Steps**:
1. Send PUT request for non-existent cart item
2. Verify error response

**Expected Result**:
- Status Code: 404 Not Found
- Error response contains:
  - errorCode = "PRODUCT_NOT_FOUND"
  - message indicating product not found in cart

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests cart item existence validation.

---

### TC-011: Update Quantity - Exceeds Stock

**Test Case ID**: TC-011  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: PUT /api/cart/item/{productId}

**Objective**: Verify that the system prevents updating quantity beyond available stock.

**Preconditions**:
- Application is running
- Cart contains product with ID 1
- Product has limited stock (< 1000)
- User session is valid

**Test Data**:
```json
{
  "quantity": 1000
}
```

**Headers**:
- Content-Type: application/json
- X-User-Id: 1

**Path Parameters**:
- productId: 1

**Test Steps**:
1. Send PUT request with quantity exceeding stock
2. Verify out of stock error

**Expected Result**:
- Status Code: 409 Conflict
- Error response contains:
  - errorCode = "OUT_OF_STOCK"
  - message with stock information

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests stock validation on update.

---

### TC-012: Remove Item - Happy Path

**Test Case ID**: TC-012  
**Priority**: High  
**Test Type**: Positive  
**Endpoint**: DELETE /api/cart/item/{productId}

**Objective**: Verify that an item can be successfully removed from the cart.

**Preconditions**:
- Application is running
- Cart contains product with ID 1
- User session is valid

**Test Data**: None (DELETE request)

**Headers**:
- X-User-Id: 1

**Path Parameters**:
- productId: 1

**Test Steps**:
1. Verify product is in cart (GET /api/cart)
2. Send DELETE request to /api/cart/item/1
3. Verify response status code
4. Verify item is removed from cart
5. Verify total price is recalculated
6. Verify response time

**Expected Result**:
- Status Code: 200 OK
- Response contains:
  - Updated cart without the removed product
  - Recalculated totalPrice
  - Updated itemCount
- Response time < 500ms

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests item removal functionality.

---

### TC-013: Remove Item - Product Not in Cart

**Test Case ID**: TC-013  
**Priority**: High  
**Test Type**: Negative  
**Endpoint**: DELETE /api/cart/item/{productId}

**Objective**: Verify that the system returns appropriate error when attempting to remove a product not in cart.

**Preconditions**:
- Application is running
- Product with ID 999 is not in user's cart
- User session is valid

**Test Data**: None

**Headers**:
- X-User-Id: 1

**Path Parameters**:
- productId: 999

**Test Steps**:
1. Send DELETE request for non-existent cart item
2. Verify error response

**Expected Result**:
- Status Code: 404 Not Found
- Error response contains:
  - errorCode = "PRODUCT_NOT_FOUND"
  - message indicating product not found in cart

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests error handling for remove operation.

---

### TC-014: Clear Cart - Happy Path

**Test Case ID**: TC-014  
**Priority**: High  
**Test Type**: Positive  
**Endpoint**: DELETE /api/cart

**Objective**: Verify that all items can be successfully removed from the cart at once.

**Preconditions**:
- Application is running
- Cart contains one or more items
- User session is valid

**Test Data**: None

**Headers**:
- X-User-Id: 1

**Test Steps**:
1. Verify cart has items (GET /api/cart)
2. Send DELETE request to /api/cart
3. Verify response status code
4. Verify cart is empty
5. Verify total price is 0
6. Verify response time

**Expected Result**:
- Status Code: 200 OK
- Response contains:
  - items = empty array []
  - totalPrice = 0.0
  - itemCount = 0
- Response time < 500ms

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests bulk removal functionality.

---

### TC-015: Clear Empty Cart

**Test Case ID**: TC-015  
**Priority**: Medium  
**Test Type**: Positive  
**Endpoint**: DELETE /api/cart

**Objective**: Verify that clearing an already empty cart works without errors.

**Preconditions**:
- Application is running
- Cart is already empty
- User session is valid

**Test Data**: None

**Headers**:
- X-User-Id: 1

**Test Steps**:
1. Ensure cart is empty
2. Send DELETE request to /api/cart
3. Verify response

**Expected Result**:
- Status Code: 200 OK
- Response contains empty cart
- No errors thrown

**Actual Result**: [To be filled during execution]

**Status**: [Pass/Fail]

**Notes**: Tests edge case of clearing empty cart.

---

## Validation Rules Tested

### Product Validation
- ✓ Product must exist in database (TC-002)
- ✓ Product must be in stock (TC-004)
- ✓ Product must be available (implicit in all tests)

### Quantity Validation
- ✓ Quantity must be >= 1 (TC-003, TC-009)
- ✓ Quantity must not exceed available stock (TC-004, TC-011)
- ✓ Quantity increment works correctly (TC-005)

### Cart Operations
- ✓ Add new item to cart (TC-001)
- ✓ Increment existing item quantity (TC-005)
- ✓ Update item quantity (TC-008)
- ✓ Remove item from cart (TC-012)
- ✓ Clear entire cart (TC-014)
- ✓ Retrieve cart contents (TC-006)

### Error Handling
- ✓ Product not found (TC-002, TC-010, TC-013)
- ✓ Validation errors (TC-003, TC-009)
- ✓ Out of stock errors (TC-004, TC-011)
- ✓ Cart operation errors (implicit)

### Performance
- ✓ Response time < 500ms (all positive tests)

---

## Test Execution Guidelines

### Prerequisites
1. Start the Spring Boot application
2. Ensure H2 database is initialized with sample products
3. Import Postman collection and environment
4. Set environment variables:
   - baseUrl: http://localhost:8080/api
   - userId: 1

### Execution Order
1. Clear cart (TC-014) - Start with clean state
2. Add items (TC-001, TC-002, TC-003, TC-004, TC-005)
3. Get cart (TC-006, TC-007)
4. Update quantities (TC-008, TC-009, TC-010, TC-011)
5. Remove items (TC-012, TC-013)
6. Clear cart (TC-014, TC-015)

### Test Data Setup
Sample products initialized in database:
1. Wireless Mouse - $29.99 (50 in stock)
2. Mechanical Keyboard - $89.99 (30 in stock)
3. USB-C Hub - $45.50 (25 in stock)
4. Laptop Stand - $35.00 (40 in stock)
5. Webcam HD - $79.99 (20 in stock)

### Expected Coverage
- **Endpoints Covered**: 5/5 (100%)
- **HTTP Methods**: POST, GET, PUT, DELETE
- **Positive Tests**: 8
- **Negative Tests**: 7
- **Total Test Cases**: 15

---

## Traceability Matrix

| Requirement | Test Cases | Coverage |
|-------------|------------|----------|
| Add product to cart | TC-001, TC-002, TC-003, TC-004, TC-005 | 100% |
| Increment quantity if exists | TC-005 | 100% |
| Show cart contents | TC-006, TC-007 | 100% |
| Update item quantity | TC-008, TC-009, TC-010, TC-011 | 100% |
| Remove item from cart | TC-012, TC-013 | 100% |
| Clear cart | TC-014, TC-015 | 100% |
| Validate product existence | TC-002, TC-010, TC-013 | 100% |
| Validate product stock | TC-004, TC-011 | 100% |
| Validate quantity | TC-003, TC-009 | 100% |
| Response time < 500ms | All positive tests | 100% |

---

## Notes

1. **Session Management**: All tests use X-User-Id header for user identification
2. **Data Persistence**: Cart persists across requests for the same user
3. **Concurrency**: Tests assume single-user execution; concurrent tests require separate test suite
4. **Error Codes**: All error responses follow standard format with errorCode, message, and details
5. **Performance**: All operations must complete within 500ms as per NFR

---

## Test Artifacts

- **Postman Collection**: test/postman/collection.json
- **Postman Environment**: test/postman/environment.json
- **Test Cases Document**: test/api_test_cases.md (this file)
- **Execution Report**: test/reports/execution_report.md

---

**Document Version**: 1.0  
**Last Updated**: 2024-01-15  
**Author**: QA Automation Agent  
**Status**: Ready for Execution
