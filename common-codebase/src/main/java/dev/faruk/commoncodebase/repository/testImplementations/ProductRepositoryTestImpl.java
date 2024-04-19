package dev.faruk.commoncodebase.repository.testImplementations;

import dev.faruk.commoncodebase.entity.Product;
import dev.faruk.commoncodebase.repository.base.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryTestImpl implements ProductRepository {
    private final List<Product> cache = new ArrayList<>();
    private long idCounter = 1;

    @Override
    public List<Product> findAll() {
        return cache;
    }

    @Override
    public Product findById(Long id) {
        return cache.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Product create(Product product) {
        product.setId(idCounter++);
        cache.add(product);
        return product;
    }
}
