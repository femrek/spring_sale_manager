package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = {"sale"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sale_product_bridge", schema = "public")
public class SaleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Sale.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "unit_price")
    private Double unitPrice;
}
