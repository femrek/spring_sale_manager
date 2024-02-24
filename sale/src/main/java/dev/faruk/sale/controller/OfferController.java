package dev.faruk.sale.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.sale.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offer")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    /**
     * This method lists all offers saved in the database.
     * @return List of all offers
     */
    @GetMapping({"/", ""})
    public AppSuccessResponse<List<OfferDTO>> showAll() {
        final List<OfferDTO> offers = offerService.findAll();
        return new AppSuccessResponse<>("All offers are listed successfully", offers);
    }

    /**
     * This method shows an offer by its id.
     * @param id the id of the offer to be shown
     * @return the offer with the given id
     */
    @GetMapping("/{id}")
    public AppSuccessResponse<OfferDTO> showById(@PathVariable(name = "id") int id) {
        final OfferDTO offer = offerService.findById(id);
        return new AppSuccessResponse<>("The offer is shown by id successfully", offer);
    }
}
