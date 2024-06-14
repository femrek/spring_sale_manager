package dev.faruk.sale.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.sale.dto.SalePostRequest;
import dev.faruk.sale.service.SaleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/sale")
public class SaleController {
    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * This method creates a sale.
     *
     * @param salePostRequest the request body including the sale data
     * @return the created and saved sale data
     */
    @PostMapping({"/", ""})
    public AppSuccessResponse<SaleDTO> create(@RequestBody SalePostRequest salePostRequest,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        final SaleDTO result = saleService.create(salePostRequest, authHeader);
        return new AppSuccessResponse<>(HttpStatus.CREATED.value(), "sale created successfully", result);
    }

    /**
     * This method previews a sale. It does not save the sale.
     *
     * @param salePostRequest the request body including the sale data
     * @param authHeader      the authorization header
     * @return the previewed sale data
     */
    @PostMapping("/preview")
    public AppSuccessResponse<SaleDTO> preview(@RequestBody SalePostRequest salePostRequest,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        final SaleDTO result = saleService.preview(salePostRequest, authHeader);
        return new AppSuccessResponse<>(HttpStatus.OK.value(), "sale previewed successfully", result);
    }

    @ExceptionHandler
    public AppSuccessResponse<ErrorResponse> handleException(Exception e) throws Exception {
        log.warn("An exception occurred: ", e);
        throw e;
    }
}
