package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.*;
import dev.faruk.commoncodebase.repository.base.OfferRepository;
import dev.faruk.commoncodebase.repository.base.ProductRepository;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaleTestDataSource {
    public static final String CASHIER_USERNAME_FOR_SALE_CREATION = "cashier_UserClient";

    private final OfferRepository offerRepository;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SaleTestDataSource(OfferRepository offerRepository,
                              SaleRepository saleRepository,
                              UserRepository userRepository,
                              ProductRepository productRepository) {
        this.offerRepository = offerRepository;
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Long> productSetA_3_2_1;
    public List<Long> productSetB_2_1;
    public List<Long> productSetC_1_1;
    public List<Long> productSetBB_1_1;

    public Long offerIdA_15_30D_3_2_1;
    public Long offerIdB_20_30D_2_1;
    public Long offerIdC_25_30D_1_1;
    public Long offerIdBB_25_30D_1_1;
    public Long offerIdD_25_passed30D_1_1;

    public void cleanDatabase() {
        List<Long> sales = saleRepository.findAll(
                1,
                10000,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        ).stream().map(Sale::getId).toList();
        for (Long id : sales) {
            saleRepository.deletePermanent(saleRepository.findById(id));
        }

        List<Long> offers = offerRepository.findAll().stream().map(Offer::getId).toList();
        for (Long id : offers) {
            offerRepository.deleteById(id);
        }

        List<Long> products = productRepository.findAll().stream().map(Product::getId).toList();
        for (Long id : products) {
            productRepository.deleteById(id);
        }

        List<Long> users = userRepository.findAll().stream().map(AppUser::getId).toList();
        for (Long id : users) {
            userRepository.deleteById(id);
        }
    }

    public void load() {
        load(false);
    }

    public void loadWithSales() {
        load(true);
    }

    public void load(boolean withSales) {
        userRepository.create(_generateCashierUser_forOnlyNewSales());

        List<Product> products = List.of(
                productRepository.create(productData.generate_productA_50()),
                productRepository.create(productData.generate_productB_155()),
                productRepository.create(productData.generate_productC_05()),
                productRepository.create(productData.generate_productD_10())
        );

        List<Product> productListA = new ArrayList<>();
        productListA.add(products.get(0));
        productListA.add(products.get(1));
        productListA.add(products.get(2));
        List<Product> productListB = new ArrayList<>();
        productListB.add(products.get(1));
        productListB.add(products.get(2));
        List<Product> productListC = new ArrayList<>();
        productListC.add(products.get(2));
        productListC.add(products.get(3));
        List<Product> productListBB = new ArrayList<>();
        productListBB.add(products.get(0));
        productListBB.add(products.get(1));


        productSetA_3_2_1 = productListA.stream().map(Product::getId).toList();
        productSetB_2_1 = productListB.stream().map(Product::getId).toList();
        productSetC_1_1 = productListC.stream().map(Product::getId).toList();
        productSetBB_1_1 = productListBB.stream().map(Product::getId).toList();

        List<Offer> offers = new ArrayList<>();
        offers.add(offerRepository.create(offerData.generate_offerA_15_30D_3_2_1(
                offerProductData.generate_offerProductA_3_2_1(productListA))));
        offers.add(offerRepository.create(offerData.generate_offerB_20_30D_2_1(
                offerProductData.generate_offerProductB_2_1(productListB))));
        offers.add(offerRepository.create(offerData.generate_offerC_25_30D_1_1(
                offerProductData.generate_offerProductC_1_1(productListC))));
        offers.add(offerRepository.create(offerData.generate_offerC_25_30D_1_1(
                offerProductData.generate_offerProductC_1_1(productListBB))));
        offers.add(offerRepository.create(offerData.generate_offerD_25_passed30D_1_1(
                offerProductData.generate_offerProductC_1_1(productListBB))));

        offerIdA_15_30D_3_2_1 = offers.get(0).getId();
        offerIdB_20_30D_2_1 = offers.get(1).getId();
        offerIdC_25_30D_1_1 = offers.get(2).getId();
        offerIdBB_25_30D_1_1 = offers.get(3).getId();
        offerIdD_25_passed30D_1_1 = offers.get(4).getId();

        if (withSales) {
            saleRepository.create(saleData.generate_sale(
                    offers,
                    cashierData.generateCashierUser(_generateCashierRoles()),
                    saleProductData.generate_productSetA_3_2_1(productListA)
            ));
            saleRepository.create(saleData.generate_sale(
                    offers,
                    cashierData.generateCashierUser(_generateCashierRoles()),
                    saleProductData.generate_productSetB_2_1(productListB)
            ));
            saleRepository.create(saleData.generate_sale(
                    offers,
                    cashierData.generateCashierUser(_generateCashierRoles()),
                    saleProductData.generate_productSetC_1_1(productListC)
            ));
        }
    }

    private AppUser _generateCashierUser_forOnlyNewSales() {
        return AppUser.builder()
                .username(CASHIER_USERNAME_FOR_SALE_CREATION)
                .password("password")
                .name("Cashier of Sale Create Request")
                .roles(_generateCashierRoles())
                .build();
    }

    private List<AppUserRole> _generateCashierRoles() {
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(userRepository.findRoleCashier());
        return roles;
    }

    private static final SaleData saleData = new SaleData();

    private static final CashierData cashierData = new CashierData();

    private static final OfferData offerData = new OfferData();

    private static final SaleProductData saleProductData = new SaleProductData();

    private static final OfferProductData offerProductData = new OfferProductData();

    private static final ProductData productData = new ProductData();
}
