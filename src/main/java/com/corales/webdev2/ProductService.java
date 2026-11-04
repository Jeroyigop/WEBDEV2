package com.corales.webdev2;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopProperties shopProperties;

    public ProductService(ProductRepository productRepository,
                          ShopProperties shopProperties) {
        this.productRepository = productRepository;
        this.shopProperties = shopProperties;
    }

    public List<Product> getProductsAbovePrice(double threshold) {
        return productRepository.getProducts()
                .stream()
                .filter(product -> product.getPrice() > threshold)
                .toList();
    }

    public String getShopName() {
        return shopProperties.getName();
    }

    public String getCurrency() {
        return shopProperties.getCurrency();
    }
}