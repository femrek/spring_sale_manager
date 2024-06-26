package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sale", schema = "public")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "received_money")
    private Double receivedMoney;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = AppUser.class)
    @JoinColumn(name = "cashier")
    private AppUser cashier;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = SaleProduct.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_id")
    private List<SaleProduct> productList;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Offer.class)
    @JoinTable(
            name = "sale_offer",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    private List<Offer> offers;

    public void add(SaleProduct saleProduct) {
        if (productList == null) productList = new ArrayList<>();
        productList.add(saleProduct);
    }

    public void add(Offer offer) {
        if (offers == null) offers = new ArrayList<>();
        offers.add(offer);
    }

    /**
     * Returns the visible column names that are exposed to the client. Also, these names are used in the repository
     * layer to filter the columns that are going to be selected from the database.
     *
     * @return Set of visible column names
     */
    public static Set<String> getVisibleColumns() {
        return Set.of(
                "id",
                "receivedMoney",
                "createdAt",
                "cashier"
        );
    }
}
