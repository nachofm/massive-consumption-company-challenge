package com.company.challenge.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("01-products")
public class Product {
    @Id
    private ObjectId _id;
    private int id;
    private String brand;
    private String description;
    private String image;
    private float price;
}
