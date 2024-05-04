package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.Product;
import dev.faruk.commoncodebase.entity.SaleProduct;

import java.util.ArrayList;
import java.util.List;

public class SaleProductData {
    public List<SaleProduct> generate_productSetA_3_2_1(List<Product> products) {
        List<SaleProduct> result = new ArrayList<>();
        result.add(SaleProduct.builder()
                .productCount(3)
                .product(products.get(0))
                .build());
        result.add(SaleProduct.builder()
                .productCount(2)
                .product(products.get(1))
                .build());
        result.add(SaleProduct.builder()
                .productCount(1)
                .product(products.get(2))
                .build());
        return result;
    }

    public List<SaleProduct> generate_productSetB_2_1(List<Product> products) {
        List<SaleProduct> result = new ArrayList<>();
        result.add(SaleProduct.builder()
                .productCount(2)
                .product(products.get(0))
                .build());
        result.add(SaleProduct.builder()
                .productCount(1)
                .product(products.get(1))
                .build());
        return result;
    }

    public List<SaleProduct> generate_productSetC_1_1(List<Product> products) {
        List<SaleProduct> result = new ArrayList<>();
        result.add(SaleProduct.builder()
                .productCount(1)
                .product(products.get(0))
                .build());
        result.add(SaleProduct.builder()
                .productCount(1)
                .product(products.get(1))
                .build());
        return result;
    }
}
