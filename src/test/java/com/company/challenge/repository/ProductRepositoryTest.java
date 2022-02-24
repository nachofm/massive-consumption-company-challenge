package com.company.challenge.repository;

import com.company.challenge.model.Product;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataMongoTest
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    private Product savedProduct1;
    private Product savedProduct2;
    private List<Product> savedProductsList;

    @BeforeEach
    void setUp() {
        savedProductsList = new ArrayList<>();
        savedProduct1 = Product.builder()
                ._id(new ObjectId())
                .id(1)
                .brand("Samsung")
                .description("Samsung Smart TV")
                .image("www.lider.cl/catalogo/images/samsungSmartTV.svg")
                .price(600000f)
                .build();
        savedProduct2 = Product.builder()
                ._id(new ObjectId())
                .id(2)
                .brand("Philips Smart")
                .description("Philips TV")
                .image("www.lider.cl/catalogo/images/philipsSmartTV.svg")
                .price(500000f)
                .build();
        productRepository.save(savedProduct1);
        productRepository.save(savedProduct2);
        savedProductsList.add(savedProduct1);
        savedProductsList.add(savedProduct2);
    }

    @Test
    void shouldReturnFirstSavedProduct() {
        when(productRepository.findFirstProductById(1)).thenReturn(savedProduct1);

        Product firstFoundProduct = productRepository.findFirstProductById(1);

        assertNotNull(firstFoundProduct);
        assertNotNull(firstFoundProduct.get_id());
        assertEquals(firstFoundProduct.getId(), 1);
        assertEquals(firstFoundProduct.getBrand(), "Samsung");
        assertEquals(firstFoundProduct.getDescription(), "Samsung Smart TV");
        assertEquals(firstFoundProduct.getImage(), "www.lider.cl/catalogo/images/samsungSmartTV.svg");
        assertEquals(firstFoundProduct.getPrice(), 600000.0, 0);
    }

    @Test
    void shouldGetProductByBrandAndDescriptionSuccessfully() {
        when(productRepository.getProductByBrandAndDescription("Smart")).thenReturn(savedProductsList);

        List<Product> productList = productRepository.getProductByBrandAndDescription("Smart");

        assertNotNull(productList);
        assertNotNull(productList.get(0).get_id());
        assertEquals(productList.get(0).getId(), 1);
        assertEquals(productList.get(0).getBrand(), "Samsung");
        assertEquals(productList.get(0).getDescription(), "Samsung Smart TV");
        assertEquals(productList.get(0).getImage(), "www.lider.cl/catalogo/images/samsungSmartTV.svg");
        assertEquals(productList.get(0).getPrice(), 600000.0, 0);

        assertNotNull(productList.get(1).get_id());
        assertEquals(productList.get(1).getId(), 2);
        assertEquals(productList.get(1).getBrand(), "Philips Smart");
        assertEquals(productList.get(1).getDescription(), "Philips TV");
        assertEquals(productList.get(1).getImage(), "www.lider.cl/catalogo/images/philipsSmartTV.svg");
        assertEquals(productList.get(1).getPrice(), 500000.0, 0);
    }
}