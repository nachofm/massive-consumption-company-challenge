package com.company.challenge.service;

import com.company.challenge.model.Product;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface IProductService {

    List<Product> getAllProducts();

    Product getProductById(int id);

    List<Product> getProductByBrandAndDescription(String search) throws ResponseStatusException;

    List<Product> applyDiscountToProductList(List<Product> searchResult);

    Product applyDiscountToSingleProduct(Product searchResult);

    boolean shouldApplyDiscount(String search);
}
