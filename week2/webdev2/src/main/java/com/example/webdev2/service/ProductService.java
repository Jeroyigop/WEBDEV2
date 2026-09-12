package com.example.webdev2.service;

import com.example.webdev2.config.ShopProperties;
import com.example.webdev2.model.Product;
import com.example.webdev2.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopProperties shopProperties;

    public ProductService(ProductRepository productRepository, ShopProperties shopProperties) {
        this.productRepository = productRepository;
        this.shopProperties = shopProperties;
    }

    public List<Product> getProductsAbovePrice(double minPrice) {
        return productRepository.findAll().stream()
                .filter(product -> product.getPrice() > minPrice)
                .collect(Collectors.toList());
    }

    public String getShopName() {
        return shopProperties.getName();
    }

    public String getShopCurrency() {
        return shopProperties.getCurrency();
    }
}