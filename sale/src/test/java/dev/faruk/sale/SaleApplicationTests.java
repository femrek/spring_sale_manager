package dev.faruk.sale;

import dev.faruk.commoncodebase.dto.OfferDTO;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.entity.Product;
import dev.faruk.commoncodebase.repository.ProductRepository;
import dev.faruk.commoncodebase.repository.UserRepository;
import dev.faruk.sale.dto.SalePostRequest;
import dev.faruk.sale.service.OfferService;
import dev.faruk.sale.service.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SaleApplicationTests {
    private final OfferService offerService;
    private final SaleService saleService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public SaleApplicationTests(OfferService offerService, SaleService saleService, ProductRepository productRepository, UserRepository userRepository) {
        this.offerService = offerService;
        this.saleService = saleService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * This test checks if the offer fetch functionality works correctly.
     */
    @Test
    void offerFetch() {
        List<OfferDTO> offers = offerService.findAll();
        assert !offers.isEmpty();

        OfferDTO offer = offerService.findById(offers.get(0).getId());
        assert offer != null;
        assert offer.getId() == offers.get(0).getId();
    }


    /**
     * This test checks if the sale create functionality works correctly.
     */
    @Test
    void saleCreateAndFetch() {
        // prepare the data for try to save a sale
        List<Product> products = productRepository.findAll();
        if (products.size() < 2) {
            throw new RuntimeException("No products found. add products to the database before testing");
        }

        // create sale model
        final SalePostRequest salePostRequest = new SalePostRequest(1000.0, findOrCreateTestCashier());
        salePostRequest.add(new SalePostRequest.ProductDetails(products.get(0).getId(), 4));
        salePostRequest.add(new SalePostRequest.ProductDetails(products.get(1).getId(), 2));

        // save sale and check if it exists in database.
        final SaleDTO savedSale = saleService.create(salePostRequest);

        try {
            assert saleService.findById(savedSale.getId()) != null;
            assert savedSale.getProducts().size() == 2;
            assert savedSale.getProducts().size() == salePostRequest.getProducts().size();
        } catch (AssertionError ignored) {
            // fail the test
            assert false;
        } finally {
            // delete the test sale
            saleService.deletePermanentById(savedSale.getId());
        }
    }

    /**
     * Finds the test cashier if exists, otherwise creates the test cashier user.
     *
     * @return id of the test cashier
     */
    private Long findOrCreateTestCashier() {
        // check if the test cashier exists. if exists, return its id
        final String testCashierUsername = "testCashier";
        final AppUser existingCashier = userRepository.findByUsername(testCashierUsername);
        if (existingCashier != null) {
            return existingCashier.getId();
        }

        // create the test cashier
        final AppUser testCashier = new AppUser(
                testCashierUsername,
                "testPassword"
        );
        AppUserRole cashierRole = userRepository.findRoleCashier();
        testCashier.add(cashierRole);

        // save the test cashier and return its id
        return userRepository.create(testCashier).getId();
    }
}
