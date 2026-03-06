package com.myproject.repositories;

import com.myproject.models.entities.Cart;
import com.myproject.models.repositories.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void testSaveCart() {
        Cart cart = Cart.builder()
                .userId(1L)
                .totalPrice(0.0)
                .build();

        Cart savedCart = cartRepository.save(cart);

        assertNotNull(savedCart.getId());
        assertEquals(1L, savedCart.getUserId());
    }

    @Test
    void testFindByUserId() {
        Cart cart = Cart.builder()
                .userId(1L)
                .totalPrice(0.0)
                .build();
        cartRepository.save(cart);

        Optional<Cart> foundCart = cartRepository.findByUserId(1L);

        assertTrue(foundCart.isPresent());
        assertEquals(1L, foundCart.get().getUserId());
    }

    @Test
    void testExistsByUserId() {
        Cart cart = Cart.builder()
                .userId(1L)
                .totalPrice(0.0)
                .build();
        cartRepository.save(cart);

        boolean exists = cartRepository.existsByUserId(1L);

        assertTrue(exists);
    }

    @Test
    void testDeleteCart() {
        Cart cart = Cart.builder()
                .userId(1L)
                .totalPrice(0.0)
                .build();
        Cart savedCart = cartRepository.save(cart);

        cartRepository.deleteById(savedCart.getId());

        Optional<Cart> deletedCart = cartRepository.findById(savedCart.getId());
        assertFalse(deletedCart.isPresent());
    }
}
