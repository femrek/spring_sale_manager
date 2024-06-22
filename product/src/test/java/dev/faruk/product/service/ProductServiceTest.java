package dev.faruk.product.service;

import dev.faruk.commoncodebase.dto.ProductDTO;
import dev.faruk.product.ProductTestConfigurer;
import dev.faruk.product.ProductTestDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductTestConfigurer.class)
class ProductServiceTest {
    private final ProductService productService;
    private final ProductTestDataSource productTestDataSource;

    @Autowired
    public ProductServiceTest(ProductService productService,
                              ProductTestDataSource productTestDataSource) {
        this.productService = productService;
        this.productTestDataSource = productTestDataSource;
    }

    @BeforeEach
    void setUp() {
        productTestDataSource.cleanDatabase();
        productTestDataSource.loadDatabase();
    }

    @AfterEach
    void tearDown() {
        productTestDataSource.cleanDatabase();
    }

    @Test
    void getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        assertEquals(5, products.size());
    }

    @Test
    void getProductById() {
        List<ProductDTO> products = productService.getAllProducts();
        for (ProductDTO product : products) {
            ProductDTO productById = productService.getProductById(product.getId());
            assertNotNull(productById);
            assertEquals(product.getId(), productById.getId());
            assertEquals(product.getName(), productById.getName());
            assertEquals(product.getPrice(), productById.getPrice());
            assertEquals(product.getBarcode(), productById.getBarcode());
        }
    }
}
