package dev.faruk.sale.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalePostRequest {
    private Double receivedMoney;
    private List<ProductDetails> products;
    private List<Long> offerIds;

    public void add(ProductDetails productDetails) {
        if (products == null) products = new ArrayList<>();
        products.add(productDetails);
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public final class ProductDetails {
        private Long productId;
        private Integer productCount;
    }
}
