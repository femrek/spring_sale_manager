package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.sale.SaleTestConfigurations;
import dev.faruk.sale.datasource.SaleTestDataSource;
import dev.faruk.sale.dto.SalePostRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SaleTestConfigurations.class)
class SaleServiceTest {
    private final SaleService saleService;
    private final SaleRepository saleRepository;
    private final SaleTestDataSource saleTestDataSource;

    @Autowired
    public SaleServiceTest(SaleService saleService,
                           SaleRepository saleRepository,
                           SaleTestDataSource saleTestDataSource) {
        this.saleService = saleService;
        this.saleRepository = saleRepository;
        this.saleTestDataSource = saleTestDataSource;
    }

    @BeforeEach
    void setUp() {
        saleTestDataSource.cleanDatabase();
        saleTestDataSource.load();
    }

    @AfterEach
    void tearDown() {
        saleTestDataSource.cleanDatabase();
    }

    @Test
    void create_success_theSameProductListWithTheOffer() {
        SalePostRequest salePostRequest = prepareSuccessWithTheSameProductList();
        SaleDTO createdSale = saleService.create(salePostRequest, "Bearer token");
        assertSaleSuccessWithTheSameProductList(createdSale);

        List<Sale> sales = saleRepository.findAll(
                1,
                10000,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(1, sales.size());
    }

    @Test
    void create_success_differentProductListSize() {
        SalePostRequest salePostRequest = prepareSuccess_differentProductListSize();
        SaleDTO createdSale = saleService.create(salePostRequest, "Bearer token");
        assertSaleSuccessWithDifferentProductListSize(createdSale);

        List<Sale> sales = saleRepository.findAll(
                1,
                10000,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(1, sales.size());
    }

    @Test
    void create_receivedMoneyDoesNotEnough() {
        SalePostRequest salePostRequest = prepareReceivedMoneyDoesNotEnough();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.create(salePostRequest, "Bearer token"));
    }

    @Test
    void create_offerDoesNotSatisfied_differentProductsWithTheSameListSize() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentProductsWithTheSameListSize();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.create(salePostRequest, "Bearer token"));
    }

    @Test
    void create_offerDoesNotSatisfied_differentCountOfAProduct() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentCountOfAProduct();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.create(salePostRequest, "Bearer token"));
    }

    @Test
    void create_offerDoesNotSatisfied_differentProductListSize_smallerRequirements() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentProductListSize_smallerRequirements();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.create(salePostRequest, "Bearer token"));
    }

    @Test
    void create_offerDoesNotSatisfied_differentProductListSize_largerRequirements() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentProductListSize_largerRequirements();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.create(salePostRequest, "Bearer token"));
    }

    @Test
    void create_offerDoesNotSatisfied_expiredOffer() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_expiredOffer();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.create(salePostRequest, "Bearer token"));
    }

    @Test
    void preview_success_theSameProductListWithTheOffer() {
        SalePostRequest salePostRequest = prepareSuccessWithTheSameProductList();
        SaleDTO previewedSale = saleService.preview(salePostRequest, "Bearer token");
        assertSaleSuccessWithTheSameProductList(previewedSale);

        List<Sale> sales = saleRepository.findAll(
                1,
                10000,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(0, sales.size());
    }

    @Test
    void preview_success_differentProductListSize() {
        SalePostRequest salePostRequest = prepareSuccess_differentProductListSize();
        SaleDTO previewedSale = saleService.preview(salePostRequest, "Bearer token");
        assertSaleSuccessWithDifferentProductListSize(previewedSale);

        List<Sale> sales = saleRepository.findAll(
                1,
                10000,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(0, sales.size());
    }

    @Test
    void preview_receivedMoneyDoesNotEnough() {
        SalePostRequest salePostRequest = prepareReceivedMoneyDoesNotEnough();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.preview(salePostRequest, "Bearer token"));
    }

    @Test
    void preview_offerDoesNotSatisfied_differentProductsWithTheSameListSize() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentProductsWithTheSameListSize();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.preview(salePostRequest, "Bearer token"));
    }

    @Test
    void preview_offerDoesNotSatisfied_differentCountOfAProduct() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentCountOfAProduct();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.preview(salePostRequest, "Bearer token"));
    }

    @Test
    void preview_offerDoesNotSatisfied_differentProductListSize_smallerRequirements() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentProductListSize_smallerRequirements();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.preview(salePostRequest, "Bearer token"));
    }

    @Test
    void preview_offerDoesNotSatisfied_differentProductListSize_largerRequirements() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_differentProductListSize_largerRequirements();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.preview(salePostRequest, "Bearer token"));
    }

    @Test
    void preview_offerDoesNotSatisfied_expiredOffer() {
        SalePostRequest salePostRequest = prepareOfferDoesNotSatisfied_expiredOffer();
        assertThrows(AppHttpError.BadRequest.class, () -> saleService.preview(salePostRequest, "Bearer token"));
    }

    private SalePostRequest prepareSuccessWithTheSameProductList() {
        final Integer[] requiredProductCounts = {3, 2, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetA_3_2_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();

        return SalePostRequest.builder()
                .receivedMoney(1000D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdA_15_30D_3_2_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareSuccess_differentProductListSize() {
        final Integer[] requiredProductCounts = {3, 2, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetA_3_2_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(1000D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdB_20_30D_2_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareReceivedMoneyDoesNotEnough() {
        final Integer[] requiredProductCounts = {3, 2, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetA_3_2_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(100D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdA_15_30D_3_2_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareOfferDoesNotSatisfied_differentProductsWithTheSameListSize() {
        final Integer[] requiredProductCounts = {1, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetC_1_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(500D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdBB_25_30D_1_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareOfferDoesNotSatisfied_differentCountOfAProduct() {
        final Integer[] requiredProductCounts = {2, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetB_2_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(500D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdBB_25_30D_1_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareOfferDoesNotSatisfied_differentProductListSize_smallerRequirements() {
        final Integer[] requiredProductCounts = {3, 2, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetA_3_2_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(500D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdC_25_30D_1_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareOfferDoesNotSatisfied_differentProductListSize_largerRequirements() {
        final Integer[] requiredProductCounts = {2, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetB_2_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(500D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdA_15_30D_3_2_1
                ))
                .products(products)
                .build();
    }

    private SalePostRequest prepareOfferDoesNotSatisfied_expiredOffer() {
        final Integer[] requiredProductCounts = {1, 1};
        List<SalePostRequest.ProductDetails> products = saleTestDataSource.productSetBB_1_1.stream()
                .map(productId -> SalePostRequest.ProductDetails.builder()
                        .productId(productId)
                        .productCount(requiredProductCounts[0]++)
                        .build()
                ).toList();
        return SalePostRequest.builder()
                .receivedMoney(1000D)
                .offerIds(List.of(
                        saleTestDataSource.offerIdD_25_passed30D_1_1
                ))
                .products(products)
                .build();
    }

    private void assertSaleSuccessWithTheSameProductList(SaleDTO saleDTO) {
        assertTrue(saleDTO.getOffers().size() < 2);
        Double discount = 0.0;
        if (!saleDTO.getOffers().isEmpty()) {
            discount = saleDTO.getOffers().get(0).getDiscount();
        }
        Double totalExpected = 772.5D * (1.0D - discount);
        Double changeExpected = 1000 - totalExpected;
        assertEquals(1000, saleDTO.getReceivedMoney());
        assertEquals(totalExpected, saleDTO.getTotal());
        assertEquals(changeExpected, saleDTO.getChange());
        assertEquals(3, saleDTO.getProducts().size());
    }

    private void assertSaleSuccessWithDifferentProductListSize(SaleDTO saleDTO) {
        assertTrue(saleDTO.getOffers().size() < 2);
        Double discount = 0.0;
        if (!saleDTO.getOffers().isEmpty()) {
            discount = saleDTO.getOffers().get(0).getDiscount();
        }
        Double totalExpected = 772.5D * (1.0D - discount);
        Double changeExpected = 1000 - totalExpected;
        assertEquals(1000, saleDTO.getReceivedMoney());
        assertEquals(totalExpected, saleDTO.getTotal());
        assertEquals(changeExpected, saleDTO.getChange());
        assertEquals(3, saleDTO.getProducts().size());
    }
}
