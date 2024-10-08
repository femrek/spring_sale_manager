package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
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
     * This method lists all active offers saved in the database according to validSince and validUntil fields.
     *
     * @return turns the active offer entities into offer data transfer objects and returns them as a list
     */
    public List<OfferDTO> findAllActive() {
        final List<Offer> offers = offerRepository.findAllActive();
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
        if (offer == null) {
            log.debug("Offer could not found by id: %d".formatted(id));
            throw new AppHttpError.NotFound(String.format("Offer could not found by id: %d", id));
        }
        return new OfferDTO(offer);
    }
}
