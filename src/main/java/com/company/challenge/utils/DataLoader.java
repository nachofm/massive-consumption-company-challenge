package com.company.challenge.utils;

import com.company.challenge.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.challenge.model.Product;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

@Component
public class DataLoader {

    @Value("classpath:init-data.json")
    private Resource resourceFile;

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void loadData() throws IOException {
        loadSampleData();
    }

    private void loadSampleData() throws IOException {
        if (productRepository.findAll().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> productList = mapper.readValue(new ClassPathResource(Objects.requireNonNull(resourceFile.getFilename())).getInputStream(),
                                                         new TypeReference<List<Product>>() {
                                                         });
            productList.forEach(product -> product.set_id(new ObjectId()));
            productRepository.saveAll(productList);
        }
    }

}
