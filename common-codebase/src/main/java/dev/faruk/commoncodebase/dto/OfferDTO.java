package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.Offer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {
    private int id;
    private String name;
    private float discount;
    private String validUntil;
    private String validSince;
    private List<OfferProductDTO> requiredProducts;

    public OfferDTO(Offer offer) {
        this.id = offer.getId();
        this.name = offer.getName();
        this.discount = offer.getDiscount();
        this.validUntil = offer.getValidUntil().toString();
        this.validSince = offer.getValidSince().toString();
        this.requiredProducts = offer.getRequiredProducts().stream().map(OfferProductDTO::new).toList();
    }
}
