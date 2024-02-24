package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offer_requirement_bridge", schema = "public")
public class OfferProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Offer.class)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "required_count")
    private int requiredCount;
}