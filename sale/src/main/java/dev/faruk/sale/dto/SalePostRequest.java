package dev.faruk.sale.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalePostRequest {
    private Double receivedMoney;
    private List<ProductDetails> products;

    public void add(ProductDetails productDetails) {
        if (products == null) products = new ArrayList<>();
        products.add(productDetails);
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static public final class ProductDetails {
        private Long productId;
        private Integer productCount;
    }
}
