package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.SaleProduct;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleProductDTO {
    private int id;
    private ProductDTO product;
    private int productCount;
    private double unitPrice;

    public SaleProductDTO(SaleProduct saleProduct) {
        this.id = saleProduct.getId();
        this.product = new ProductDTO(saleProduct.getProduct());
        this.productCount = saleProduct.getProductCount();
        this.unitPrice = saleProduct.getUnitPrice();
    }
}
