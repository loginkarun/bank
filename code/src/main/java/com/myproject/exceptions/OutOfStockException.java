package com.myproject.exceptions;

public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(String message) {
        super(message);
    }
    
    public OutOfStockException(Long productId, Integer requestedQuantity, Integer availableStock) {
        super(String.format("Product %d is out of stock. Requested: %d, Available: %d", 
                productId, requestedQuantity, availableStock));
    }
}
