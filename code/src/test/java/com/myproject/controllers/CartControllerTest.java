package com.myproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.models.dtos.*;
import com.myproject.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    private CartResponse cartResponse;
    private CartItemAddRequest addRequest;
    private UpdateQuantityRequest updateRequest;

    @BeforeEach
    void setUp() {
        CartItemDTO item1 = CartItemDTO.builder()
                .id(1L)
                .productId(1L)
                .productName("Wireless Mouse")
                .quantity(2)
                .price(29.99)
                .subtotal(59.98)
                .build();

        cartResponse = CartResponse.builder()
                .id(1L)
                .userId(1L)
                .items(Arrays.asList(item1))
                .totalPrice(59.98)
                .itemCount(2)
                .build();

        addRequest = new CartItemAddRequest(1L, 1, 1L);
        updateRequest = new UpdateQuantityRequest(3);
    }

    @Test
    void testAddToCart_Success() throws Exception {
        when(cartService.addToCart(any(CartItemAddRequest.class), eq(1L)))
                .thenReturn(cartResponse);

        mockMvc.perform(post("/api/cart/add")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalPrice").value(59.98))
                .andExpect(jsonPath("$.itemCount").value(2));
    }

    @Test
    void testAddToCart_ValidationError() throws Exception {
        CartItemAddRequest invalidRequest = new CartItemAddRequest(null, 0, 1L);

        mockMvc.perform(post("/api/cart/add")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCart_Success() throws Exception {
        when(cartService.getCart(1L)).thenReturn(cartResponse);

        mockMvc.perform(get("/api/cart")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalPrice").value(59.98));
    }

    @Test
    void testRemoveFromCart_Success() throws Exception {
        when(cartService.removeFromCart(1L, 1L)).thenReturn(cartResponse);

        mockMvc.perform(delete("/api/cart/item/1")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateQuantity_Success() throws Exception {
        when(cartService.updateQuantity(eq(1L), any(UpdateQuantityRequest.class), eq(1L)))
                .thenReturn(cartResponse);

        mockMvc.perform(put("/api/cart/item/1")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testClearCart_Success() throws Exception {
        CartResponse emptyCart = CartResponse.builder()
                .id(1L)
                .userId(1L)
                .items(Arrays.asList())
                .totalPrice(0.0)
                .itemCount(0)
                .build();

        when(cartService.clearCart(1L)).thenReturn(emptyCart);

        mockMvc.perform(delete("/api/cart")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(0.0))
                .andExpect(jsonPath("$.itemCount").value(0));
    }
}
