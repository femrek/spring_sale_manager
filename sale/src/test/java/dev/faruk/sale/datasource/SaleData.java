package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.*;

import java.util.List;

public class SaleData {
    public Sale generate_sale(List<Offer> offers,
                              AppUser cashierUser,
                              List<SaleProduct> saleProducts) {
        return Sale.builder()
                .receivedMoney(100.0)
                .cashier(cashierUser)
                .productList(saleProducts)
                .offers(offers)
                .build();
    }
}
