package dev.faruk.commoncodebase.repository.implementation;

import dev.faruk.commoncodebase.entity.Product;
import dev.faruk.commoncodebase.repository.base.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final EntityManager entityManager;

    @Autowired
    public ProductRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    @Override
    public Product findById(Long id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    @Transactional
    public Product create(Product product) {
        entityManager.persist(product);
        entityManager.merge(product);
        return product;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Product product = entityManager.find(Product.class, id);
        entityManager.remove(product);
    }
}
