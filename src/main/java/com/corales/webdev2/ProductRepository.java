package com.corales.webdev2;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final List<Product> products = List.of(
            new Product("Laptop", 45000),
            new Product("Mouse", 800),
            new Product("Keyboard", 1500),
            new Product("Monitor", 12000),
            new Product("Headset", 2500),
            new Product("Printer", 8500)
    );

    public List<Product> getProducts() {
        return products;
    }
}