package com.myproject.config;

import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products for testing
        if (productRepository.count() == 0) {
            productRepository.save(new Product(null, "Laptop", 999.99, 10));
            productRepository.save(new Product(null, "Mouse", 29.99, 50));
            productRepository.save(new Product(null, "Keyboard", 79.99, 30));
            productRepository.save(new Product(null, "Monitor", 299.99, 15));
            productRepository.save(new Product(null, "Headphones", 149.99, 25));
        }
    }
}