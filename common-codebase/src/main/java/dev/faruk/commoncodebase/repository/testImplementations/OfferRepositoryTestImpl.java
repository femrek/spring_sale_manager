package dev.faruk.commoncodebase.repository.testImplementations;

import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OfferRepositoryTestImpl implements OfferRepository {
    private final List<Offer> cache = new ArrayList<>();
    private long idCounter = 1;

    @Override
    public List<Offer> findAll() {
        return cache;
    }

    @Override
    public List<Offer> findAllActive() {
        return cache.stream().filter(offer -> offer.getValidSince().toInstant().isBefore(new Date().toInstant())
                && offer.getValidUntil().toInstant().isAfter(new Date().toInstant())).toList();
    }

    @Override
    public Offer findById(Long id) {
        return cache.stream().filter(offer -> offer.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Offer create(Offer offer) {
        offer.setId(idCounter++);
        cache.add(offer);
        return offer;
    }

    @Override
    public void deleteById(Long id) {
        cache.removeIf(offer -> offer.getId().equals(id));
    }
}
