package dev.faruk.sale.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.sale.dto.SalePostRequest;
import dev.faruk.sale.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
