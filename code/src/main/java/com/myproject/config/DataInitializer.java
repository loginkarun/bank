package com.myproject.config;

import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder()
                    .name("Wireless Mouse")
                    .price(29.99)
                    .stock(50)
                    .available(true)
                    .build());

            productRepository.save(Product.builder()
                    .name("Mechanical Keyboard")
                    .price(89.99)
                    .stock(30)
                    .available(true)
                    .build());

            productRepository.save(Product.builder()
                    .name("USB-C Hub")
                    .price(45.50)
                    .stock(25)
                    .available(true)
                    .build());

            productRepository.save(Product.builder()
                    .name("Laptop Stand")
                    .price(35.00)
                    .stock(40)
                    .available(true)
                    .build());

            productRepository.save(Product.builder()
                    .name("Webcam HD")
                    .price(79.99)
                    .stock(20)
                    .available(true)
                    .build());

            System.out.println("Sample products initialized successfully!");
        }
    }
}
