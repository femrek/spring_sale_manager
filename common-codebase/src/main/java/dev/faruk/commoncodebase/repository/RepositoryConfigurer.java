package dev.faruk.commoncodebase.repository;

import dev.faruk.commoncodebase.repository.base.*;
import dev.faruk.commoncodebase.repository.implementation.*;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepositoryConfigurer {
    @Bean
    @Primary
    public ProductRepository productRepository(EntityManager entityManager) {
        return new ProductRepositoryImpl(entityManager);
    }

    @Bean
    @Primary
    public UserRepository userRepository(EntityManager entityManager) {
        return new UserRepositoryImpl(entityManager);
    }

    @Bean
    @Primary
    public OfferRepository offerRepository(EntityManager entityManager) {
        return new OfferRepositoryImpl(entityManager);
    }

    @Bean
    @Primary
    public SaleRepository saleRepository(EntityManager entityManager) {
        return new SaleRepositoryImpl(entityManager);
    }
}
