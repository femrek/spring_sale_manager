package dev.faruk.report.service;

import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.report.ReportTestConfigurer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ReportTestConfigurer.class)
class ReportServiceTest {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ReportService reportService;

    @Autowired
    public ReportServiceTest(SaleRepository saleRepository,
                             UserRepository userRepository,
                             ReportService reportService) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.reportService = reportService;
    }

    @BeforeEach
    public void setUpEach() {
        // reset sales
        List<Sale> sales = saleRepository.findAll(
                null,
                null,
                null,
                true,
                null,
                null,
                null,
                null,
                null
        );
        final int saleSize = sales.size();
        for (int i = saleSize - 1; i >= 0; i--) {
            saleRepository.deletePermanent(sales.get(i));
        }

        // reset users
        List<AppUser> users = userRepository.findAll();
        final int userSize = users.size();
        for (int i = userSize - 1; i >= 0; i--) {
            userRepository.deleteByUsername(users.get(i).getUsername());
        }
    }

    @Test
    public void reportService_whenShowSalesCalled_thenReturnsSavedSales() {
        addSaleSet0();
        List<SaleDTO> sales = reportService.showSales(
                1,
                100,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(2, sales.size());
    }

    @Test
    public void reportService_whenShowSalesCalledWithOrderBy_thenReturnsSavedSalesInOrder() {
        addSaleSet0();
        List<SaleDTO> sales = reportService.showSales(
                1,
                100,
                "receivedMoney",
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(100D, sales.get(0).getReceivedMoney());
    }

    @Test
    public void reportService_whenShowSalesCalledWithCashierFilter_thenReturnsSavedSalesByCashier() {
        addSaleSet0();
        AppUser sampleCashier = userRepository.findByUsername("sampleCashier");
        List<SaleDTO> sales = reportService.showSales(
                1,
                100,
                null,
                null,
                null,
                null,
                sampleCashier.getId(),
                null,
                null
        );
        assertEquals(2, sales.size());
    }

    @Test
    public void reportService_whenShowSalesCalledWithNotExistCashierFilter_thenReturnsNoSale() {
        addSaleSet0();
        userRepository.create(AppUser.builder()
                .username("sampleCashier2")
                .name("Sample Cashier 2")
                .password("sampleCashierPassword2")
                .roles(List.of(userRepository.findRoleCashier()))
                .build());
        AppUser sampleCashierWithNotSale = userRepository.findByUsername("sampleCashier2");
        List<SaleDTO> sales = reportService.showSales(
                1,
                100,
                null,
                null,
                null,
                null,
                sampleCashierWithNotSale.getId(),
                null,
                null
        );
        userRepository.deleteByUsername("sampleCashier2");
        assertEquals(0, sales.size());
    }

    @Test
    public void reportService_whenShowSalesCalledWithReceivedMoneyFilter_thenReturnsSavedSalesByReceivedMoney() {
        addSaleSet0();
        List<SaleDTO> sales = reportService.showSales(
                1,
                100,
                null,
                null,
                null,
                null,
                null,
                100D,
                150D
        );
        assertEquals(1, sales.size());
        assertEquals(100D, sales.get(0).getReceivedMoney());
    }

    @Test
    public void reportService_whenShowSalesCalledWithPagination_thenReturnsPaginatedSales() {
        addSaleSet0();
        List<SaleDTO> sales = reportService.showSales(
                1,
                1,
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
    public void reportService_whenShowSaleCalled_thenReturnsSale() {
        addSaleSet0();
        List<SaleDTO> sales = reportService.showSales(
                1,
                100,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        Long saleId = sales.get(0).getId();
        SaleDTO saleFromService = reportService.showSale(saleId);
        assertEquals(saleId, saleFromService.getId());
    }

    private void addSaleSet0() {
        AppUserRole cashierRole = userRepository.findRoleCashier();
        AppUser sampleCashier = AppUser.builder()
                .username("sampleCashier")
                .name("Sample Cashier")
                .password("sampleCashierPassword")
                .roles(List.of(cashierRole))
                .build();
        Sale sale0 = Sale.builder()
                .receivedMoney(200D)
                .cashier(sampleCashier)
                .productList(List.of())
                .paymentMethod(saleRepository.findPaymentMethodById(1L))
                .build();
        Sale sale1 = Sale.builder()
                .receivedMoney(100D)
                .cashier(sampleCashier)
                .productList(List.of())
                .paymentMethod(saleRepository.findPaymentMethodById(1L))
                .build();
        userRepository.create(sampleCashier);
        saleRepository.create(sale0);
        saleRepository.create(sale1);
    }
}