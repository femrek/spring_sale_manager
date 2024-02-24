package dev.faruk.commoncodebase.repository;

import dev.faruk.commoncodebase.entity.Sale;

import java.util.List;

public interface SaleRepository {
    /**
     * Saves a sale to the database
     * @param sale sale to be saved
     */
    Sale create(Sale sale);

    /**
     * Fetches all the sales saved in the database
     * @return List of sales
     */
    List<Sale> findAll();

    /**
     * Fetches a sale by id
     * @param id id of the sale
     * @return Sale with the given id
     */
    Sale findById(int id);

    /**
     * Deletes given sale from database.
     * @param sale the sale to be deleted.
     */
    void deletePermanent(Sale sale);
}
