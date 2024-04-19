package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
