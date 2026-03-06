package com.myproject.services;

import com.myproject.exceptions.CartOperationException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.*;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.impl.CartServiceImpl;
import com.myproject.services.interfaces.ProductService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product testProduct;
    private Cart testCart;
    private CartItemAddRequest addRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1L, "Test Product", 29.99, 10);
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUserId(1L);
        testCart.setItems(new ArrayList<>());
        testCart.setTotalPrice(0.0);
        
        addRequest = new CartItemAddRequest(1L, 2);
    }

    @Test
    void testAddItemToCart_NewItem_Success() {
        when(productService.getProductById(1L)).thenReturn(testProduct);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartResponse response = cartService.addItemToCart(1L, addRequest);

        assertNotNull(response);
        verify(productService).validateProductAvailability(1L, 2);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_ProductNotFound_ThrowsException() {
        doThrow(new ProductNotFoundException(1L))
            .when(productService).validateProductAvailability(anyLong(), anyInt());

        assertThrows(ProductNotFoundException.class, () -> {
            cartService.addItemToCart(1L, addRequest);
        });
    }

    @Test
    void testAddItemToCart_OutOfStock_ThrowsException() {
        doThrow(new OutOfStockException(1L))
            .when(productService).validateProductAvailability(anyLong(), anyInt());

        assertThrows(OutOfStockException.class, () -> {
            cartService.addItemToCart(1L, addRequest);
        });
    }

    @Test
    void testGetCart_ExistingCart_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        CartResponse response = cartService.getCart(1L);

        assertNotNull(response);
        assertEquals(0.0, response.getTotalPrice());
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void testGetCart_NewCart_CreatesEmptyCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        CartResponse response = cartService.getCart(1L);

        assertNotNull(response);
        assertEquals(0.0, response.getTotalPrice());
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void testRemoveItemFromCart_Success() {
        CartItem item = new CartItem(1L, "Test Product", 29.99, 2);
        testCart.addItem(item);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartItemRemoveRequest removeRequest = new CartItemRemoveRequest(1L);
        CartResponse response = cartService.removeItemFromCart(1L, removeRequest);

        assertNotNull(response);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testRemoveItemFromCart_CartNotFound_ThrowsException() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        CartItemRemoveRequest removeRequest = new CartItemRemoveRequest(1L);
        assertThrows(CartOperationException.class, () -> {
            cartService.removeItemFromCart(1L, removeRequest);
        });
    }

    @Test
    void testUpdateItemQuantity_Success() {
        CartItem item = new CartItem(1L, "Test Product", 29.99, 2);
        testCart.addItem(item);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartItemUpdateRequest updateRequest = new CartItemUpdateRequest(1L, 5);
        CartResponse response = cartService.updateItemQuantity(1L, updateRequest);

        assertNotNull(response);
        verify(productService).validateProductAvailability(1L, 5);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testUpdateItemQuantity_ProductNotInCart_ThrowsException() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        CartItemUpdateRequest updateRequest = new CartItemUpdateRequest(999L, 5);
        assertThrows(ProductNotFoundException.class, () -> {
            cartService.updateItemQuantity(1L, updateRequest);
        });
    }
}