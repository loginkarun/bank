package com.myproject.controllers;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemRemoveRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.interfaces.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * POST /api/cart/add - Add item to cart
     * Corresponds to OpenAPI operation: SCRUM-11692_add_item_to_cart_post
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId,
            @Valid @RequestBody CartItemAddRequest request) {
        CartResponse response = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/cart - Get cart
     * Corresponds to OpenAPI operation: SCRUM-11692_get_cart_get
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/cart/item - Remove item from cart
     * Corresponds to OpenAPI operation: SCRUM-11692_remove_item_from_cart_delete
     */
    @DeleteMapping("/item")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId,
            @Valid @RequestBody CartItemRemoveRequest request) {
        CartResponse response = cartService.removeItemFromCart(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/cart/item - Update item quantity
     * Corresponds to OpenAPI operation: SCRUM-11692_update_item_quantity_put
     */
    @PutMapping("/item")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId,
            @Valid @RequestBody CartItemUpdateRequest request) {
        CartResponse response = cartService.updateItemQuantity(userId, request);
        return ResponseEntity.ok(response);
    }
}