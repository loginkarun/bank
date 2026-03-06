package com.myproject.services.interfaces;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.dtos.UpdateQuantityRequest;

public interface CartService {
    
    CartResponse addToCart(CartItemAddRequest request, Long userId);
    
    CartResponse getCart(Long userId);
    
    CartResponse removeFromCart(Long productId, Long userId);
    
    CartResponse updateQuantity(Long productId, UpdateQuantityRequest request, Long userId);
    
    CartResponse clearCart(Long userId);
}
