package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offer", schema = "public")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "offer_name")
    private String name;

    @Column(name = "discount")
    private Double discount;

    @Column(name ="valid_until")
    private Timestamp validUntil;

    @Column(name ="valid_since")
    private Timestamp validSince;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = OfferProduct.class)
    @JoinColumn(name = "offer_id")
    private List<OfferProduct> requiredProducts;

    public Offer(String name, Double discount, Timestamp validUntil, Timestamp validSince) {
        this.name = name;
        this.discount = discount;
        this.validUntil = validUntil;
        this.validSince = validSince;
    }

    public void add(OfferProduct offerProduct) {
        if (requiredProducts == null) requiredProducts = new ArrayList<>();
        requiredProducts.add(offerProduct);
    }
}
