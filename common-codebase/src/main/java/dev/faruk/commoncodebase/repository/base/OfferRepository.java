package dev.faruk.commoncodebase.repository.base;

import dev.faruk.commoncodebase.entity.Offer;

import java.util.List;

public interface OfferRepository {
    /**
     * @return all offers saved in the database
     */
    List<Offer> findAll();

    /**
     * @return all active offers saved in the database according to validSince and validUntil fields.
     */
    List<Offer> findAllActive();

    /**
     * @param id the id of the offer to be found
     * @return the offer with the given id, or null if it does not exist
     */
    Offer findById(Long id);

    /**
     * @param offer the offer to be saved
     * @return the saved offer
     */
    Offer create(Offer offer);

    /**
     * removes the offer with given id from the database
     * @param id the id of the offer to be deleted
     */
    void deleteById(Long id);
}
