package com.company.challenge.controller;

import com.company.challenge.model.Product;
import com.company.challenge.service.IProductService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllProducts/")
    public @ResponseBody Iterable<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/getProductById/{id}")
    public @ResponseBody
    Product getProductById(@PathVariable(value = "id") int id) {
        Product searchResult = productService.getProductById(id);
        if (shouldApplyDiscount(id, searchResult)) {
            searchResult = productService.applyDiscountToSingleProduct(searchResult);
        }
        return searchResult;
    }

    private boolean shouldApplyDiscount(int id, Product searchResult) {
        return searchResult != null && productService.shouldApplyDiscount(String.valueOf(id));
    }

    @GetMapping("/getProductByBrandAndDescription")
    public @ResponseBody
    Iterable<Product> getProductByBrandAndDescription(@RequestParam(value = "desc", required = false) String query) throws
            ResponseStatusException {
        List<Product> searchResult = productService.getProductByBrandAndDescription(query);
        if ((!searchResult.isEmpty()) && productService.shouldApplyDiscount(query)) {
            productService.applyDiscountToProductList(searchResult);
        }
        return searchResult;
    }
}
