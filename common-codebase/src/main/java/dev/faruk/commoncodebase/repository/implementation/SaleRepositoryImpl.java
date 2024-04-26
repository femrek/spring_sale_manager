package dev.faruk.commoncodebase.repository.implementation;

import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class SaleRepositoryImpl implements SaleRepository {
    private final EntityManager entityManager;

    @Autowired
    public SaleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Sale create(Sale sale) {
        entityManager.persist(sale);
        entityManager.flush();
        return sale;
    }

    @Override
    public List<Sale> findAll(final Integer page,
                              final Integer size,
                              final String orderBy,
                              final Boolean orderAsc,
                              final Long dateFilterAfter,
                              final Long dateFilterBefore,
                              final Long cashierFilterId,
                              final Double receivedMoneyFilterMin,
                              final Double receivedMoneyFilterMax) {
        final boolean orderAscFinal = orderAsc == null || orderAsc;

        // prepare query
        StringBuilder queryStrBuilder = new StringBuilder("SELECT s FROM Sale as s WHERE true");
        if (receivedMoneyFilterMin != null) queryStrBuilder.append(" AND s.receivedMoney >= :received_money_min");
        if (receivedMoneyFilterMax != null) queryStrBuilder.append(" AND s.receivedMoney <= :received_money_max");
        if (cashierFilterId != null) queryStrBuilder.append(" AND s.cashier.id = :cashier_id");
        if (dateFilterAfter != null) queryStrBuilder.append(" AND s.createdAt >= :date_min");
        if (dateFilterBefore != null) queryStrBuilder.append(" AND s.createdAt <= :date_max");

        if (orderBy != null) queryStrBuilder.append(" ORDER BY :order_by");
        if (orderAscFinal) queryStrBuilder.append(" asc");
        else queryStrBuilder.append(" desc");

        // create query
        TypedQuery<Sale> query = entityManager.createQuery(queryStrBuilder.toString(), Sale.class);

        // set parameters
        if (receivedMoneyFilterMin != null) query.setParameter("received_money_min", receivedMoneyFilterMin);
        if (receivedMoneyFilterMax != null) query.setParameter("received_money_max", receivedMoneyFilterMax);
        if (cashierFilterId != null) query.setParameter("cashier_id", cashierFilterId);
        if (dateFilterAfter != null) query.setParameter("date_min", new Timestamp(dateFilterAfter));
        if (dateFilterBefore != null) query.setParameter("date_max", new Timestamp(dateFilterBefore));
        if (orderBy != null) query.setParameter("order_by", orderBy);

        // pagination
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Sale findById(Long id) {
        TypedQuery<Sale> query = entityManager.createQuery(
                "SELECT s FROM Sale as s WHERE s.id = :sale_id",
                Sale.class);
        query.setParameter("sale_id", id);

        final List<Sale> sales = query.getResultList();
        if (sales.isEmpty()) return null;
        return sales.get(0);
    }

    @Override
    @Transactional
    public void deletePermanent(Sale sale) {
        entityManager.createQuery("DELETE FROM Sale as s WHERE s.id = :sale_id")
                .setParameter("sale_id", sale.getId())
                .executeUpdate();
    }
}
