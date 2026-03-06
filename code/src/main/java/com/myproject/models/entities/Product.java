package com.myproject.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public boolean hasStock(int quantity) {
        return stock != null && stock >= quantity;
    }

    public void decreaseStock(int quantity) {
        if (hasStock(quantity)) {
            this.stock -= quantity;
        } else {
            throw new IllegalStateException("Insufficient stock");
        }
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
}