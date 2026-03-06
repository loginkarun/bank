package com.myproject.services;

import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.dtos.UpdateQuantityRequest;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartItemRepository;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product product;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Wireless Mouse")
                .price(29.99)
                .stock(50)
                .available(true)
                .build();

        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .items(new ArrayList<>())
                .totalPrice(0.0)
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .cart(cart)
                .productId(1L)
                .productName("Wireless Mouse")
                .quantity(1)
                .price(29.99)
                .build();
    }

    @Test
    void testAddToCart_NewItem_Success() {
        CartItemAddRequest request = new CartItemAddRequest(1L, 2, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.addToCart(request, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_ProductNotFound() {
        CartItemAddRequest request = new CartItemAddRequest(999L, 1, 1L);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            cartService.addToCart(request, 1L);
        });
    }

    @Test
    void testAddToCart_OutOfStock() {
        CartItemAddRequest request = new CartItemAddRequest(1L, 100, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(OutOfStockException.class, () -> {
            cartService.addToCart(request, 1L);
        });
    }

    @Test
    void testAddToCart_IncrementExistingItem() {
        CartItemAddRequest request = new CartItemAddRequest(1L, 1, 1L);
        cart.getItems().add(cartItem);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.addToCart(request, 1L);

        assertNotNull(response);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testGetCart_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCart(1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
    }

    @Test
    void testRemoveFromCart_Success() {
        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.removeFromCart(1L, 1L);

        assertNotNull(response);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testUpdateQuantity_Success() {
        UpdateQuantityRequest request = new UpdateQuantityRequest(3);
        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.updateQuantity(1L, request, 1L);

        assertNotNull(response);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testClearCart_Success() {
        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.clearCart(1L);

        assertNotNull(response);
        assertEquals(0, response.getItemCount());
        assertEquals(0.0, response.getTotalPrice());
    }
}
