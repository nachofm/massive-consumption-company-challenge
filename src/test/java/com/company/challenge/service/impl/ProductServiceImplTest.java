package com.company.challenge.service.impl;

import com.company.challenge.repository.ProductRepository;
import com.company.challenge.model.Product;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceImplTest {

    private Product product1;
    private Product product2;
    private List<Product> productList;
    private float originalPriceForProduct1;
    private float originalPriceForProduct2;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        originalPriceForProduct1 = 498725;
        originalPriceForProduct2 = 130173;
        productList = new ArrayList<>();
        product1 = Product.builder()
                ._id(new ObjectId())
                .id(1)
                .brand("ooy eqrceli")
                .description("dsaasd")
                .image("www.lider.cl/catalogo/images/whiteLineIcon.svg")
                .price(originalPriceForProduct1)
                .build();
        product2 = Product.builder()
                ._id(new ObjectId())
                .id(2)
                .brand("dsaasd")
                .description("zlrwax bñyrh")
                .image("www.lider.cl/catalogo/images/babyIcon.svg")
                .price(originalPriceForProduct2)
                .build();

        productList.add(product1);
        productList.add(product2);
    }

    @Test
    void shouldGetAllProductsSuccessfully() {
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> productList1 = productService.getAllProducts();

        assertEquals(productList1, productList);
        Mockito.verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldGetProductByIdSuccessfully() {
        int id = 1;
        Mockito.when(productRepository.findFirstProductById(id)).thenReturn(product1);

        Product productResult = productService.getProductById(id);

        assertEquals(productService.getProductById(product1.getId()), productResult);
    }

    @Test
    void shouldGetProductsByBrandAndDescriptionSuccessfully() {
        String searchTerm = "saas";
        when(productRepository.getProductByBrandAndDescription(searchTerm)).thenReturn(productList);

        List<Product> productList1 = productService.getProductByBrandAndDescription(searchTerm);

        assertEquals(productList1, productList);
        Mockito.verify(productRepository, times(1)).getProductByBrandAndDescription(searchTerm);
    }

    @ParameterizedTest
    @ValueSource(strings = { "a", "aa" })
    void shouldThrowExceptionWhenSearchStringIsLessThanThreeCharacters(String searchTerm) {
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            productService.getProductByBrandAndDescription(searchTerm);
        });

        String expectedMessage = "Minimum amount of chars for searching is three (3). Please search again using at least three (3) characters.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(((ResponseStatusException) exception).getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldApplyDiscountToSingleProductSuccessfully() {
        float discountedPrice = originalPriceForProduct1 / 2;

        Product discountedProduct = productService.applyDiscountToSingleProduct(product1);

        assertNotNull(discountedProduct);
        assertEquals(discountedProduct.getPrice(), discountedPrice);
    }

    @Test
    void shouldApplyDiscountToProductListSuccessfully() {
        float discountedPriceForProduct1 = originalPriceForProduct1 / 2;
        float discountedPriceForProduct2 = originalPriceForProduct2 / 2;

        List<Product> discountedProductList = productService.applyDiscountToProductList(productList);

        assertNotNull(discountedProductList);
        assertEquals(discountedProductList.get(0).getPrice(), discountedPriceForProduct1);
        assertEquals(discountedProductList.get(1).getPrice(), discountedPriceForProduct2);

    }

    @ParameterizedTest
    @ValueSource(strings = { "191", "abba" })
    void shouldApplyDiscountBasedOnSearchSuccessfully(String search) {

        assertTrue(productService.shouldApplyDiscount(search));
    }

    @ParameterizedTest
    @ValueSource(strings = { "123", "hello" })
    void shouldNotApplyDiscountBasedOnSearchSuccessfully(String search) {

        assertFalse(productService.shouldApplyDiscount(search));
    }

}