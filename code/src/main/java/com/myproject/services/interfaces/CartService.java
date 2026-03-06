package com.myproject.services.interfaces;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemRemoveRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;

public interface CartService {

    CartResponse addItemToCart(Long userId, CartItemAddRequest request);

    CartResponse getCart(Long userId);

    CartResponse removeItemFromCart(Long userId, CartItemRemoveRequest request);

    CartResponse updateItemQuantity(Long userId, CartItemUpdateRequest request);
}