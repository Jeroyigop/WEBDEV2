package com.corales.webdev2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final List<Product> products = new ArrayList<>();

    public ProductController() {
        products.add(new Product(1L, "Laptop", 45000));
        products.add(new Product(2L, "Monitor", 12000));
        products.add(new Product(3L, "Printer", 8500));
    }

    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {

        for (Product product : products) {
            if (product.getId().equals(id)) {
                model.addAttribute("product", product);
                return "product-detail";
            }
        }

        return "product-detail";
    }

    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping
    public String createProduct(
            @ModelAttribute Product product,
            BindingResult result) {

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            result.rejectValue("name", "name.empty", "Name is required.");
        }

        if (product.getPrice() <= 0) {
            result.rejectValue("price", "price.invalid", "Price must be greater than 0.");
        }

        if (result.hasErrors()) {
            return "product-form";
        }

        long nextId = products.size() + 1;
        product.setId(nextId);

        products.add(product);

        return "redirect:/products";
    }
}