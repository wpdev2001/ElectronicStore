package com.wp.estore.service;

import com.wp.estore.dtos.ProductDto;
import com.wp.estore.entities.Product;
import com.wp.estore.repositories.ProductRepository;
import com.wp.estore.services.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    public void init() {
        product = Product.builder()
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
    void createProductTest() {

        when(productRepository.save(any())).thenReturn(product);


        // assertions: id and date assigned


        // fields copied from DTO to entity


        // returned DTO contains the same id

    }
}
