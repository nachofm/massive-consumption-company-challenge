package com.company.challenge.controller;

import com.company.challenge.service.impl.ProductServiceImpl;
import com.company.challenge.model.Product;

import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {
    @Mock
    private ProductServiceImpl productService;
    private Product product1;
    private Product product2;
    private List<Product> productList;
    private int searchId;
    private String searchTerms;

    @InjectMocks
    private ProductController productController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        openMocks(this);
        product1 = Product.builder()
                ._id(new ObjectId())
                .id(1)
                .brand("ooy eqrceli")
                .description("dsaasd")
                .image("www.lider.cl/catalogo/images/whiteLineIcon.svg")
                .price(498724)
                .build();
        product2 = Product.builder()
                ._id(new ObjectId())
                .id(121)
                .brand("dsaasd")
                .description("ooy eqrceli")
                .image("www.lider.cl/catalogo/images/whiteLineIcon.svg")
                .price(376822)
                .build();
        productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void shouldGetASingleProductSuccessfully() throws Exception {
        searchId = 1;
        when(productService.getProductById(searchId)).thenReturn(product1);
        mockMvc.perform(get("/getProductById/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.brand", Matchers.is("ooy eqrceli")))
                .andExpect(jsonPath("$.description", Matchers.is("dsaasd")))
                .andExpect(jsonPath("$.image", Matchers.is("www.lider.cl/catalogo/images/whiteLineIcon.svg")))
                .andExpect(jsonPath("$.price", Matchers.is(498724.0)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));

        verify(productService).getProductById(searchId);
        verify(productService, times(1)).getProductById(searchId);
    }

    @Test
    void shouldGetASingleProductWithDiscountSuccessfully() throws Exception {
        searchId = 121;
        Product productWithDiscountedPrice = getSingleProductWithDiscountedPrice(product2);
        when(productService.getProductById(searchId)).thenReturn(product2);
        when(productService.shouldApplyDiscount(String.valueOf(searchId))).thenReturn(true);
        when(productService.applyDiscountToSingleProduct(product2)).thenReturn(productWithDiscountedPrice);
        mockMvc.perform(get("/getProductById/" + searchId)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.is(121)))
                .andExpect(jsonPath("$.brand", Matchers.is("dsaasd")))
                .andExpect(jsonPath("$.description", Matchers.is("ooy eqrceli")))
                .andExpect(jsonPath("$.image", Matchers.is("www.lider.cl/catalogo/images/whiteLineIcon.svg")))
                .andExpect(jsonPath("$.price", Matchers.is(188411.0)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));

        verify(productService).getProductById(searchId);
        verify(productService, times(1)).getProductById(searchId);
    }

    @Test
    void shouldGetProductListSuccessfully() throws Exception {
        searchTerms = "ooy";
        when(productService.getProductByBrandAndDescription(searchTerms)).thenReturn(productList);
        mockMvc.perform(get("/getProductByBrandAndDescription")
                        .param("desc", searchTerms)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]._id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].brand", Matchers.is("ooy eqrceli")))
                .andExpect(jsonPath("$.[0].description", Matchers.is("dsaasd")))
                .andExpect(jsonPath("$.[0].image", Matchers.is("www.lider.cl/catalogo/images/whiteLineIcon.svg")))
                .andExpect(jsonPath("$.[0].price", Matchers.is(498724.0)))
                .andExpect(jsonPath("$.[0].*", Matchers.hasSize(6)))

                .andExpect(jsonPath("$.[1]._id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.[1].id", Matchers.is(121)))
                .andExpect(jsonPath("$.[1].brand", Matchers.is("dsaasd")))
                .andExpect(jsonPath("$.[1].description", Matchers.is("ooy eqrceli")))
                .andExpect(jsonPath("$.[1].image", Matchers.is("www.lider.cl/catalogo/images/whiteLineIcon.svg")))
                .andExpect(jsonPath("$.[1].price", Matchers.is(376822.0)))
                .andExpect(jsonPath("$.[1].*", Matchers.hasSize(6)));

        verify(productService).getProductByBrandAndDescription(searchTerms);
        verify(productService, times(1)).getProductByBrandAndDescription(searchTerms);
    }

    @Test
    void shouldGetProductListWithDiscountsSuccessfully() throws Exception {
        searchTerms = "dsaasd";
        List<Product> productListWithDiscounts = getProductListWithDiscounts(productList);
        when(productService.getProductByBrandAndDescription(searchTerms)).thenReturn(productList);
        when(productService.shouldApplyDiscount(searchTerms)).thenReturn(true);
        when(productService.applyDiscountToProductList(productList)).thenReturn(productListWithDiscounts);
        mockMvc.perform(get("/getProductByBrandAndDescription")
                        .param("desc", searchTerms)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]._id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].brand", Matchers.is("ooy eqrceli")))
                .andExpect(jsonPath("$.[0].description", Matchers.is("dsaasd")))
                .andExpect(jsonPath("$.[0].image", Matchers.is("www.lider.cl/catalogo/images/whiteLineIcon.svg")))
                .andExpect(jsonPath("$.[0].price", Matchers.is(249362.0)))
                .andExpect(jsonPath("$.[0].*", Matchers.hasSize(6)))

                .andExpect(jsonPath("$.[1]._id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.[1].id", Matchers.is(121)))
                .andExpect(jsonPath("$.[1].brand", Matchers.is("dsaasd")))
                .andExpect(jsonPath("$.[1].description", Matchers.is("ooy eqrceli")))
                .andExpect(jsonPath("$.[1].image", Matchers.is("www.lider.cl/catalogo/images/whiteLineIcon.svg")))
                .andExpect(jsonPath("$.[1].price", Matchers.is(188411.0)))
                .andExpect(jsonPath("$.[1].*", Matchers.hasSize(6)));

        verify(productService).getProductByBrandAndDescription(searchTerms);
        verify(productService, times(1)).getProductByBrandAndDescription(searchTerms);
    }

    private Product getSingleProductWithDiscountedPrice(Product product) {
        product.setPrice(product.getPrice() / 2);
        return product;
    }

    private List<Product> getProductListWithDiscounts(List<Product> productList) {
        productList.forEach(product -> product.setPrice(product.getPrice() / 2));
        return productList;
    }
}