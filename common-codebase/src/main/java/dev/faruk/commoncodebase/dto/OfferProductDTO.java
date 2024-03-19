package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.OfferProduct;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferProductDTO {
    private Long id;
    private ProductDTO product;
    private Integer requiredCount;

    public OfferProductDTO(OfferProduct offerProduct) {
        this.id = offerProduct.getId();
        this.product = new ProductDTO(offerProduct.getProduct());
        this.requiredCount = offerProduct.getRequiredCount();
    }
}
