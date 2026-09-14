package com.corales.webdev2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Webdev2Application {

    public static void main(String[] args) {
        SpringApplication.run(Webdev2Application.class, args);
    }

    @Bean
    CommandLineRunner startupReport(ProductService productService) {
        return args -> {

            double threshold = 5000;

            System.out.println("==============================");
            System.out.println("       PRODUCT REPORT");
            System.out.println("==============================");

            System.out.println("Shop: " + productService.getShopName());
            System.out.println("Currency: " + productService.getCurrency());

            System.out.println();
            System.out.println("Products above PHP " + threshold + ":");

            productService.getProductsAbovePrice(threshold)
                    .forEach(product ->
                            System.out.println(
                                    product.getName()
                                            + " - "
                                            + productService.getCurrency()
                                            + " "
                                            + product.getPrice()
                            )
                    );

            System.out.println("==============================");
        };
    }
}