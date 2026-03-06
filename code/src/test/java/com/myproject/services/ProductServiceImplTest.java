package com.myproject.services;

import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1L, "Test Product", 29.99, 10);
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(29.99, result.getPrice());
        assertEquals(10, result.getStock());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_NotFound_ThrowsException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(999L);
        });
    }

    @Test
    void testValidateProductAvailability_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertDoesNotThrow(() -> {
            productService.validateProductAvailability(1L, 5);
        });
    }

    @Test
    void testValidateProductAvailability_OutOfStock_ThrowsException() {
        Product outOfStockProduct = new Product(1L, "Test Product", 29.99, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(outOfStockProduct));

        assertThrows(OutOfStockException.class, () -> {
            productService.validateProductAvailability(1L, 1);
        });
    }

    @Test
    void testValidateProductAvailability_InsufficientStock_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThrows(OutOfStockException.class, () -> {
            productService.validateProductAvailability(1L, 20);
        });
    }

    @Test
    void testValidateProductAvailability_ProductNotFound_ThrowsException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.validateProductAvailability(999L, 1);
        });
    }
}