package dev.faruk.commoncodebase.repository;

import dev.faruk.commoncodebase.entity.Sale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        return sale;
    }

    @Override
    public List<Sale> findAll() {
        TypedQuery<Sale> query = entityManager.createQuery("SELECT s FROM Sale as s", Sale.class);

        return query.getResultList();
    }

    @Override
    public Sale findById(int id) {
        TypedQuery<Sale> query = entityManager.createQuery("SELECT s FROM Sale as s WHERE s.id = :sale_id", Sale.class);
        query.setParameter("sale_id", id);

        final List<Sale> sales = query.getResultList();
        if (sales.isEmpty()) return null;
        return sales.get(0);
    }

    @Override
    @Transactional
    public void deletePermanent(Sale sale) {
        entityManager.remove(entityManager.contains(sale) ? sale : entityManager.merge(sale));
    }
}
