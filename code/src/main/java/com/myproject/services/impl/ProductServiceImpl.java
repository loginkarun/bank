package com.myproject.services.impl;

import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public void validateProductAvailability(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        
        if (!product.isInStock()) {
            throw new OutOfStockException(productId);
        }
        
        if (!product.hasStock(quantity)) {
            throw new OutOfStockException("Requested quantity exceeds available stock for product: " + productId);
        }
    }
}