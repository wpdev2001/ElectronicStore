package com.wp.estore.service;

import com.wp.estore.dtos.ProductDto;
import com.wp.estore.entities.Product;
import com.wp.estore.repositories.ProductRepository;
import com.wp.estore.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;


@SpringBootTest
public class ProductServiceTest {

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void init() {
        product = Product.builder()
                .productId("P1")
                .productTitle("Sample Product")
                .productDesc("Sample Description")
                .price(100)
                .discountedPrice(90)
                .quantity(10)
                .isLive(true)
                .stock(true)
                .productImage("sample.png")
                .build();
    }

    @Test
    public void createProductTest() {
        when(productRepository.save(any())).thenReturn(product);
        ProductDto product1 = productService.createProduct(mapper.map(product, ProductDto.class));
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Sample Product", product1.getProductTitle());
    }

    @Test
    public void updateProductTest(){
        String productId = "P1";
        when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productDto = mapper.map(product, ProductDto.class);
        productDto.setProductTitle("Updated Product");
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals("Updated Product", updatedProduct.getProductTitle());
    }
}
