package dev.faruk.commoncodebase.repository.implementation;

import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public Offer create(Offer offer) {
        entityManager.persist(offer);
        entityManager.merge(offer); // merge to get the id of the offer
        return offer;
    }
}
