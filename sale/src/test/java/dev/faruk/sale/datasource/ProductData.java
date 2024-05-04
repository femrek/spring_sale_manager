package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.Product;

public class ProductData {
    public Product generate_productA_50() {
        return Product.builder()
                .name("product A")
                .price(50.0)
                .barcode("90123456001")
                .build();
    }

    public Product generate_productB_155() {
        return Product.builder()
                .name("product A")
                .price(155.0)
                .barcode("90123456002")
                .build();
    }

    public Product generate_productC_05() {
        return Product.builder()
                .name("product A")
                .price(0.5)
                .barcode("90123456003")
                .build();
    }

    public Product generate_productD_10() {
        return Product.builder()
                .name("product A")
                .price(10.0)
                .barcode("90123456004")
                .build();
    }
}
