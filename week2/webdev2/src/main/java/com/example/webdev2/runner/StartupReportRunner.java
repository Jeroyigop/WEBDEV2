package com.example.webdev2.runner;

import com.example.webdev2.model.Product;
import com.example.webdev2.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupReportRunner implements CommandLineRunner {

    private final ProductService productService;

    public StartupReportRunner(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        double threshold = 5000;
        String shopName = productService.getShopName();
        String currency = productService.getShopCurrency();
        List<Product> filteredProducts = productService.getProductsAbovePrice(threshold);

        System.out.println("================================");
        System.out.println("       PRODUCT REPORT");
        System.out.println("================================");
        System.out.println("Shop: " + shopName);
        System.out.println("Currency: " + currency);
        System.out.println();
        System.out.println("Products above " + currency + " " + (int) threshold + ":");
        System.out.println();
        for (Product product : filteredProducts) {
            System.out.printf("%s - %s %.0f%n", product.getName(), currency, product.getPrice());
        }
        System.out.println("================================");
    }
}