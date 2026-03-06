package com.myproject.services.impl;

import com.myproject.exceptions.CartOperationException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.*;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartItemRepository;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartResponse addToCart(CartItemAddRequest request, Long userId) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        if (!product.getAvailable() || product.getStock() < request.getQuantity()) {
            throw new OutOfStockException(request.getProductId(), request.getQuantity(), product.getStock());
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .totalPrice(0.0)
                            .build();
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            if (newQuantity > product.getStock()) {
                throw new OutOfStockException(request.getProductId(), newQuantity, product.getStock());
            }
            
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        cart.recalculateTotal();
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .totalPrice(0.0)
                            .build();
                    return cartRepository.save(newCart);
                });

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse removeFromCart(Long productId, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartOperationException("Cart not found for user"));

        CartItem itemToRemove = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        cart.removeItem(itemToRemove);
        cartItemRepository.delete(itemToRemove);
        cart.recalculateTotal();
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse updateQuantity(Long productId, UpdateQuantityRequest request, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartOperationException("Cart not found for user"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (request.getQuantity() > product.getStock()) {
            throw new OutOfStockException(productId, request.getQuantity(), product.getStock());
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        cart.recalculateTotal();
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartOperationException("Cart not found for user"));

        cart.getItems().clear();
        cart.recalculateTotal();
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> CartItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(itemDTOs)
                .totalPrice(cart.getTotalPrice())
                .itemCount(cart.getItemCount())
                .build();
    }
}
