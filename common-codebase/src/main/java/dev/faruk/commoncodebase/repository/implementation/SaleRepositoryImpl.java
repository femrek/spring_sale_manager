package dev.faruk.commoncodebase.repository.implementation;

import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        // prepare criteria query
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sale> criteriaQuery = criteriaBuilder.createQuery(Sale.class);
        Root<Sale> SaleRoot = criteriaQuery.from(Sale.class);

        // select all
        criteriaQuery.select(SaleRoot);

        // where conditions with filters
        List<Predicate> whereConditions = new ArrayList<>();
        if (receivedMoneyFilterMin != null) {
            whereConditions.add(criteriaBuilder.greaterThanOrEqualTo(SaleRoot.get("receivedMoney"), receivedMoneyFilterMin));
        }
        if (receivedMoneyFilterMax != null) {
            whereConditions.add(criteriaBuilder.lessThanOrEqualTo(SaleRoot.get("receivedMoney"), receivedMoneyFilterMax));
        }
        if (cashierFilterId != null) {
            whereConditions.add(criteriaBuilder.equal(SaleRoot.get("cashier").get("id"), cashierFilterId));
        }
        if (dateFilterAfter != null) {
            whereConditions.add(criteriaBuilder.greaterThanOrEqualTo(SaleRoot.get("createdAt"), new Timestamp(dateFilterAfter)));
        }
        if (dateFilterBefore != null) {
            whereConditions.add(criteriaBuilder.lessThanOrEqualTo(SaleRoot.get("createdAt"), new Timestamp(dateFilterBefore)));
        }
        criteriaQuery.where(whereConditions.toArray(new Predicate[0]));

        // apply order
        List<Order> orderList = new ArrayList<>();
        if (orderAscFinal) {
            orderList.add(criteriaBuilder.asc(SaleRoot.get(Objects.requireNonNullElse(orderBy, "id"))));
        } else {
            orderList.add(criteriaBuilder.desc(SaleRoot.get(Objects.requireNonNullElse(orderBy, "id"))));
        }
        criteriaQuery.orderBy(orderList);

        // create query
        var query = entityManager.createQuery(criteriaQuery);

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
