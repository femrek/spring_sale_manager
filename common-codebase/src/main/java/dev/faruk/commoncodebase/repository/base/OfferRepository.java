package dev.faruk.commoncodebase.repository.base;

import dev.faruk.commoncodebase.entity.Offer;

import java.util.List;

public interface OfferRepository {
    /**
     * @return all offers saved in the database
     */
    List<Offer> findAll();

    /**
     * @param id the id of the offer to be found
     * @return the offer with the given id, or null if it does not exist
     */
    Offer findById(Long id);
}
