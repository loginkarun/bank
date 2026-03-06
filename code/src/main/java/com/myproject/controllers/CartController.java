package com.myproject.controllers;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.dtos.UpdateQuantityRequest;
import com.myproject.services.interfaces.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart Management", description = "Operations for managing shopping cart")
@SecurityRequirement(name = "sessionToken")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @Operation(summary = "Add item to shopping cart", 
               description = "Adds a product to the user's shopping cart. If the product already exists in the cart, increments the quantity.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item successfully added to cart",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request - validation failed"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Product out of stock"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody CartItemAddRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        
        CartResponse response = cartService.addToCart(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get shopping cart",
               description = "Retrieves the current user's shopping cart with all items, quantities, prices, and total.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - invalid session"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/item/{productId}")
    @Operation(summary = "Remove item from cart",
               description = "Removes a specific product from the shopping cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found in cart"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - invalid session"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartResponse> removeFromCart(
            @Parameter(description = "ID of the product to remove from cart", required = true)
            @PathVariable Long productId,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        
        CartResponse response = cartService.removeFromCart(productId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/item/{productId}")
    @Operation(summary = "Update item quantity in cart",
               description = "Updates the quantity of a specific product in the shopping cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quantity updated successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid quantity or out of stock"),
        @ApiResponse(responseCode = "404", description = "Product not found in cart"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - invalid session"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartResponse> updateQuantity(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long productId,
            @Valid @RequestBody UpdateQuantityRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        
        CartResponse response = cartService.updateQuantity(productId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Clear shopping cart",
               description = "Removes all items from the user's shopping cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                     content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - invalid session"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartResponse> clearCart(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        
        CartResponse response = cartService.clearCart(userId);
        return ResponseEntity.ok(response);
    }
}
