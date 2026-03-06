package com.myproject.models.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", 29.99, 10);
    }

    @Test
    void testIsInStock_WithStock_ReturnsTrue() {
        assertTrue(product.isInStock());
    }

    @Test
    void testIsInStock_WithoutStock_ReturnsFalse() {
        product.setStock(0);
        assertFalse(product.isInStock());
    }

    @Test
    void testHasStock_SufficientStock_ReturnsTrue() {
        assertTrue(product.hasStock(5));
    }

    @Test
    void testHasStock_InsufficientStock_ReturnsFalse() {
        assertFalse(product.hasStock(20));
    }

    @Test
    void testDecreaseStock_Success() {
        product.decreaseStock(3);
        assertEquals(7, product.getStock());
    }

    @Test
    void testDecreaseStock_InsufficientStock_ThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            product.decreaseStock(20);
        });
    }

    @Test
    void testIncreaseStock_Success() {
        product.increaseStock(5);
        assertEquals(15, product.getStock());
    }

    @Test
    void testProductCreation_AllFieldsSet() {
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(29.99, product.getPrice());
        assertEquals(10, product.getStock());
    }
}