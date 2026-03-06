package com.myproject.models.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private CartItem item1;
    private CartItem item2;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setUserId(1L);
        
        item1 = new CartItem(1L, "Product 1", 29.99, 2);
        item2 = new CartItem(2L, "Product 2", 49.99, 1);
    }

    @Test
    void testAddItem_Success() {
        cart.addItem(item1);
        
        assertEquals(1, cart.getItems().size());
        assertEquals(cart, item1.getCart());
        assertEquals(59.98, cart.getTotalPrice(), 0.01);
    }

    @Test
    void testAddMultipleItems_Success() {
        cart.addItem(item1);
        cart.addItem(item2);
        
        assertEquals(2, cart.getItems().size());
        assertEquals(109.97, cart.getTotalPrice(), 0.01);
    }

    @Test
    void testRemoveItem_Success() {
        cart.addItem(item1);
        cart.addItem(item2);
        cart.removeItem(item1);
        
        assertEquals(1, cart.getItems().size());
        assertEquals(49.99, cart.getTotalPrice(), 0.01);
        assertNull(item1.getCart());
    }

    @Test
    void testRecalculateTotal_Success() {
        cart.addItem(item1);
        cart.addItem(item2);
        
        item1.setQuantity(5);
        cart.recalculateTotal();
        
        assertEquals(199.94, cart.getTotalPrice(), 0.01);
    }

    @Test
    void testEmptyCart_TotalPriceZero() {
        assertEquals(0.0, cart.getTotalPrice());
        assertTrue(cart.getItems().isEmpty());
    }
}