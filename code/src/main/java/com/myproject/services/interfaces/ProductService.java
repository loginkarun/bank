package com.myproject.services.interfaces;

import com.myproject.models.entities.Product;

public interface ProductService {

    Product getProductById(Long productId);

    void validateProductAvailability(Long productId, Integer quantity);
}