package dev.faruk.commoncodebase.repository;

import dev.faruk.commoncodebase.entity.Offer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OfferRepositoryImpl implements OfferRepository {
    private final EntityManager entityManager;

    @Autowired
    public OfferRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Offer> findAll() {
        TypedQuery<Offer> query = entityManager.createQuery(
                "SELECT o FROM Offer as o", Offer.class);

        return query.getResultList();
    }

    @Override
    public Offer findById(Long id) {
        TypedQuery<Offer> query = entityManager.createQuery(
                "SELECT s FROM Offer as s WHERE id = :offer_id", Offer.class);

        query.setParameter("offer_id", id);

        final List<Offer> results = query.getResultList();
        if (results.isEmpty()) return null;
        return results.get(0);
    }
}
