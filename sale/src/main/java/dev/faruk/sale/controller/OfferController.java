package dev.faruk.sale.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.sale.service.OfferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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
    public AppSuccessResponse<List<OfferDTO>> showAll(
            @RequestParam(name = "activeOnly", required = false) Boolean active) {
        if (active == null) active = true;
        final List<OfferDTO> offers = active ? offerService.findAllActive() : offerService.findAll();
        return new AppSuccessResponse<>("All offers are listed successfully", offers);
    }

    /**
     * This method shows an offer by its id.
     * @param id the id of the offer to be shown
     * @return the offer with the given id
     */
    @GetMapping("/{id}")
    public AppSuccessResponse<OfferDTO> showById(@PathVariable(name = "id") Long id) {
        final OfferDTO offer = offerService.findById(id);
        return new AppSuccessResponse<>("The offer is shown by id successfully", offer);
    }

    @ExceptionHandler
    public AppSuccessResponse<ErrorResponse> handleException(Exception e) throws Exception {
        log.warn("An exception occurred: ", e);
        throw e;
    }
}
