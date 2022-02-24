package com.company.challenge.repository;

import com.company.challenge.model.Product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findAll();

    Product findFirstProductById(int id);

    @Query("{'$or':[ {'brand':{'$regex' : ?0, '$options' : 'i'}}, {'description':{'$regex' : ?0, '$options' : 'i'}} ]}")
    List<Product> getProductByBrandAndDescription(String search);
}