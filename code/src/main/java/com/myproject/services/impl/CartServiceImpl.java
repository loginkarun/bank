package com.myproject.services.impl;

import com.myproject.exceptions.CartOperationException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.*;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.interfaces.CartService;
import com.myproject.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Override
    public CartResponse addItemToCart(Long userId, CartItemAddRequest request) {
        // Validate product availability
        productService.validateProductAvailability(request.getProductId(), request.getQuantity());
        
        // Get product details
        Product product = productService.getProductById(request.getProductId());
        
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Increment quantity if item exists
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            // Validate new quantity against stock
            productService.validateProductAvailability(request.getProductId(), newQuantity);
            
            item.setQuantity(newQuantity);
        } else {
            // Add new item to cart
            CartItem newItem = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    request.getQuantity()
            );
            cart.addItem(newItem);
        }
        
        // Recalculate total and save
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return mapToCartResponse(savedCart);
    }

    @Override
    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });
        
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(Long userId, CartItemRemoveRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartOperationException("Cart not found for user: " + userId));
        
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart: " + request.getProductId()));
        
        cart.removeItem(itemToRemove);
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return mapToCartResponse(savedCart);
    }

    @Override
    public CartResponse updateItemQuantity(Long userId, CartItemUpdateRequest request) {
        // Validate product availability for new quantity
        productService.validateProductAvailability(request.getProductId(), request.getQuantity());
        
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartOperationException("Cart not found for user: " + userId));
        
        CartItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart: " + request.getProductId()));
        
        itemToUpdate.setQuantity(request.getQuantity());
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return mapToCartResponse(savedCart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setItems(
                cart.getItems().stream()
                        .map(item -> new CartItemDTO(
                                item.getProductId(),
                                item.getName(),
                                item.getPrice(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }
}