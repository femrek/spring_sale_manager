package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.entity.OfferProduct;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class OfferData {
    public Offer generate_offerA_15_30D_3_2_1(List<OfferProduct> offerProductSet_3_2_1) {
        return Offer.builder()
                .name("Offer A")
                .discount(0.15)
                .validSince(Timestamp.from(Instant.now().minus(Duration.ofSeconds(1))))
                .validUntil(Timestamp.from(Instant.now().plus(Duration.ofDays(30))))
                .requiredProducts(offerProductSet_3_2_1)
                .build();
    }

    public Offer generate_offerB_20_30D_2_1(List<OfferProduct> offerProductSet_2_1) {
        return Offer.builder()
                .name("Offer B")
                .discount(0.20)
                .validSince(Timestamp.from(Instant.now().minus(Duration.ofSeconds(1))))
                .validUntil(Timestamp.from(Instant.now().plus(Duration.ofDays(30))))
                .requiredProducts(offerProductSet_2_1)
                .build();
    }

    public Offer generate_offerC_25_30D_1_1(List<OfferProduct> offerProductSet_1_1) {
        return Offer.builder()
                .name("Offer C")
                .discount(0.25)
                .validSince(Timestamp.from(Instant.now().minus(Duration.ofSeconds(1))))
                .validUntil(Timestamp.from(Instant.now().plus(Duration.ofDays(30))))
                .requiredProducts(offerProductSet_1_1)
                .build();
    }

    public Offer generate_offerD_25_passed30D_1_1(List<OfferProduct> offerProductSet_1_1) {
        return Offer.builder()
                .name("Offer D")
                .discount(0.25)
                .validSince(Timestamp.from(Instant.now().minus(Duration.ofDays(30))))
                .validUntil(Timestamp.from(Instant.now().minus(Duration.ofSeconds(1))))
                .requiredProducts(offerProductSet_1_1)
                .build();
    }
}
