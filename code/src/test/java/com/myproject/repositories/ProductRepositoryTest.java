package com.myproject.repositories;

import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        Product product = Product.builder()
                .name("Test Product")
                .price(99.99)
                .stock(10)
                .available(true)
                .build();

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(99.99, savedProduct.getPrice());
    }

    @Test
    void testFindById() {
        Product product = Product.builder()
                .name("Test Product")
                .price(99.99)
                .stock(10)
                .available(true)
                .build();
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
    }

    @Test
    void testUpdateProduct() {
        Product product = Product.builder()
                .name("Test Product")
                .price(99.99)
                .stock(10)
                .available(true)
                .build();
        Product savedProduct = productRepository.save(product);

        savedProduct.setStock(5);
        Product updatedProduct = productRepository.save(savedProduct);

        assertEquals(5, updatedProduct.getStock());
    }
}
