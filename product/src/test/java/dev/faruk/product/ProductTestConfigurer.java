package dev.faruk.product;

import dev.faruk.commoncodebase.repository.base.ProductRepository;
import dev.faruk.commoncodebase.repository.testImplementations.ProductRepositoryTestImpl;
import dev.faruk.product.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductTestConfigurer {
    @Bean
    public ProductRepository productRepositoryTestImpl() {
        return new ProductRepositoryTestImpl();
    }

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }

    @Bean ProductTestDataSource productTestDataSource(ProductRepository productRepository) {
        return new ProductTestDataSource(productRepository);
    }
}
