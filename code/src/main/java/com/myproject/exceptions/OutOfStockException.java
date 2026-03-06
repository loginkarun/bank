package com.myproject.exceptions;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(Long productId) {
        super("Product is out of stock: " + productId);
    }
}