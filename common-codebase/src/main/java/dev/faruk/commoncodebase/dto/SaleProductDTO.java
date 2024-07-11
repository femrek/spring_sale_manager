package dev.faruk.commoncodebase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.faruk.commoncodebase.entity.SaleProduct;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaleProductDTO {
    private Long id;
    private ProductDTO product;
    private Integer productCount;
    private Double unitPrice;

    public SaleProductDTO(SaleProduct saleProduct) {
        this.id = saleProduct.getId();
        this.product = new ProductDTO(saleProduct.getProduct());
        this.productCount = saleProduct.getProductCount();
        this.unitPrice = saleProduct.getUnitPrice();
    }

    public Double getProductTotal() {
        return unitPrice * productCount;
    }
}
