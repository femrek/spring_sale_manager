package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.sale.SaleTestConfigurations;
import dev.faruk.sale.datasource.SaleTestDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SaleTestConfigurations.class)
class OfferServiceTest {
    private final OfferService offerService;
    private final SaleTestDataSource saleTestDataSource;

    @Autowired
    OfferServiceTest(OfferService offerService,
                     SaleTestDataSource saleTestDataSource) {
        this.offerService = offerService;
        this.saleTestDataSource = saleTestDataSource;
    }

    @BeforeEach
    void setUp() {
        saleTestDataSource.cleanDatabase();
        saleTestDataSource.loadWithSales();
    }

    @AfterEach
    void tearDown() {
        saleTestDataSource.cleanDatabase();
    }

    @Test
    void findAll() {
        List<OfferDTO> offers = offerService.findAll();
        assertEquals(5, offers.size());
    }

    @Test
    void findAllActive() {
        List<OfferDTO> offers = offerService.findAllActive();
        assertEquals(4, offers.size());
    }

    @Test
    void findById() {
        OfferDTO offer = offerService.findById(saleTestDataSource.offerIdA_15_30D_3_2_1);
        assertEquals(saleTestDataSource.offerIdA_15_30D_3_2_1, offer.getId());
    }
}