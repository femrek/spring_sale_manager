package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.OfferProduct;
import dev.faruk.commoncodebase.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class OfferProductData {
    public List<OfferProduct> generate_offerProductA_3_2_1(List<Product> products) {
        List<OfferProduct> result = new ArrayList<>();
        result.add(OfferProduct.builder()
                .requiredCount(3)
                .product(products.get(0))
                .build());
        result.add(OfferProduct.builder()
                .requiredCount(2)
                .product(products.get(1))
                .build());
        result.add(OfferProduct.builder()
                .requiredCount(1)
                .product(products.get(2))
                .build());
        return result;
    }

    public List<OfferProduct> generate_offerProductB_2_1(List<Product> products) {
        List<OfferProduct> result = new ArrayList<>();
        result.add(OfferProduct.builder()
                .requiredCount(2)
                .product(products.get(0))
                .build());
        result.add(OfferProduct.builder()
                .requiredCount(1)
                .product(products.get(1))
                .build());
        return result;
    }

    public List<OfferProduct> generate_offerProductC_1_1(List<Product> products) {
        List<OfferProduct> result = new ArrayList<>();
        result.add(OfferProduct.builder()
                .requiredCount(1)
                .product(products.get(0))
                .build());
        result.add(OfferProduct.builder()
                .requiredCount(1)
                .product(products.get(1))
                .build());
        return result;
    }
}
