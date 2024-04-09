package dev.faruk.commoncodebase.repository.base;

import dev.faruk.commoncodebase.entity.Product;

import java.util.List;

public interface ProductRepository {
    /**
     * @return all products form database.
     */
    List<Product> findAll();

    /**
     * @param id product id
     * @return the product with given id from database.
     */
    Product findById(Long id);
}
