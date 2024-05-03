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

    /**
     * @param product the product to be saved
     * @return the saved product
     */
    Product create(Product product);

    /**
     * removes the product with given id from the database
     * @param id the id of the product to be deleted
     */
    void deleteById(Long id);
}
