package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    /**
     * This method lists all offers saved in the database.
     *
     * @return turns the offer entities into offer data transfer objects and returns them as a list
     */
    public List<OfferDTO> findAll() {
        final List<Offer> offers = offerRepository.findAll();
        return offers.stream().map(OfferDTO::new).collect(Collectors.toList());
    }


    /**
     * Turns the offer with the given id into an offer data transfer object and returns it.
     *
     * @param id the id of the offer to be shown
     * @return the offer with the given id
     */
    public OfferDTO findById(Long id) {
        final Offer offer = offerRepository.findById(id);
        if (offer == null) throw new AppHttpError.NotFound(String.format("Offer could not found by id: %d", id));
        return new OfferDTO(offer);
    }
}
