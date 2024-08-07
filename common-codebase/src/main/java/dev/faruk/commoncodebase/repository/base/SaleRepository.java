package dev.faruk.commoncodebase.repository.base;

import dev.faruk.commoncodebase.entity.PaymentMethod;
import dev.faruk.commoncodebase.entity.Sale;

import java.util.List;

public interface SaleRepository {
    /**
     * Saves a sale to the database
     *
     * @param sale sale to be saved
     */
    Sale create(Sale sale);

    /**
     * Fetches all the sales saved in the database. filters and orders the sales according to the given parameters.
     *
     * @param page                   page number of the sales starting from 1. cant be null.
     * @param size                   number of sales per page. cant be null.
     * @param orderBy                order by which the sales will be sorted. this can only be a column name.
     * @param orderAsc               true if the order is ascending, false if descending. if null, defaults to true.
     * @param dateFilterAfter        minimum date filter in milliseconds since epoch
     * @param dateFilterBefore       maximum date filter in milliseconds since epoch
     * @param cashierFilterId        id of the cashier to filter
     * @param receivedMoneyFilterMin minimum received money filter
     * @param receivedMoneyFilterMax maximum received money filter
     * @return List of sales
     */
    List<Sale> findAll(
            Integer page,
            Integer size,
            String orderBy,
            Boolean orderAsc,
            Long dateFilterAfter,
            Long dateFilterBefore,
            Long cashierFilterId,
            Double receivedMoneyFilterMin,
            Double receivedMoneyFilterMax
    );

    /**
     * Fetches a sale by id
     *
     * @param id id of the sale
     * @return Sale with the given id
     */
    Sale findById(Long id);

    /**
     * Deletes given sale from database.
     *
     * @param sale the sale to be deleted.
     */
    void deletePermanent(Sale sale);

    /**
     * Fetches all the payment methods saved in the database
     *
     * @return List of payment methods
     */
    List<PaymentMethod> findAllPaymentMethods();

    /**
     * Fetches a payment method by id
     *
     * @param id id of the payment method
     * @return PaymentMethod with the given id
     */
    PaymentMethod findPaymentMethodById(Long id);
}
