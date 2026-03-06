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

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @BeforeEach
    void setUp() {
        cartResponse = new CartResponse();
        cartResponse.setItems(new ArrayList<>());
        cartResponse.setTotalPrice(0.0);
        
        addRequest = new CartItemAddRequest(1L, 2);
    }

    @Test
    void testAddItemToCart_Success() throws Exception {
        CartItemDTO item = new CartItemDTO(1L, "Test Product", 29.99, 2);
        cartResponse.setItems(Arrays.asList(item));
        cartResponse.setTotalPrice(59.98);
        
        when(cartService.addItemToCart(anyLong(), any(CartItemAddRequest.class)))
            .thenReturn(cartResponse);

        mockMvc.perform(post("/api/cart/add")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.totalPrice").value(59.98))
            .andExpect(jsonPath("$.items[0].productId").value(1));
    }

    @Test
    void testAddItemToCart_InvalidRequest_BadRequest() throws Exception {
        CartItemAddRequest invalidRequest = new CartItemAddRequest(null, -1);

        mockMvc.perform(post("/api/cart/add")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCart_Success() throws Exception {
        when(cartService.getCart(anyLong())).thenReturn(cartResponse);

        mockMvc.perform(get("/api/cart")
                .header("X-User-Id", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPrice").value(0.0))
            .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void testRemoveItemFromCart_Success() throws Exception {
        CartItemRemoveRequest removeRequest = new CartItemRemoveRequest(1L);
        when(cartService.removeItemFromCart(anyLong(), any(CartItemRemoveRequest.class)))
            .thenReturn(cartResponse);

        mockMvc.perform(delete("/api/cart/item")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(removeRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateItemQuantity_Success() throws Exception {
        CartItemUpdateRequest updateRequest = new CartItemUpdateRequest(1L, 5);
        CartItemDTO item = new CartItemDTO(1L, "Test Product", 29.99, 5);
        cartResponse.setItems(Arrays.asList(item));
        cartResponse.setTotalPrice(149.95);
        
        when(cartService.updateItemQuantity(anyLong(), any(CartItemUpdateRequest.class)))
            .thenReturn(cartResponse);

        mockMvc.perform(put("/api/cart/item")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPrice").value(149.95));
    }

    @Test
    void testUpdateItemQuantity_InvalidQuantity_BadRequest() throws Exception {
        CartItemUpdateRequest invalidRequest = new CartItemUpdateRequest(1L, 0);

        mockMvc.perform(put("/api/cart/item")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }
}