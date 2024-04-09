package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Basic(optional = false)
    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = AppUser.class)
    @JoinColumn(name = "cashier")
    private AppUser cashier;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = SaleProduct.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_id")
    private List<SaleProduct> productList;

    public Sale(Double receivedMoney, AppUser cashier, List<SaleProduct> productList) {
        this.receivedMoney = receivedMoney;
        this.cashier = cashier;
        this.productList = productList;
    }

    public Sale(Double receivedMoney, AppUser cashier) {
        this.receivedMoney = receivedMoney;
        this.cashier = cashier;
    }

    public void add(SaleProduct saleProduct) {
        if (productList == null) productList = new ArrayList<>();
        productList.add(saleProduct);
    }

    public void remove(SaleProduct saleProduct) {
        if (productList == null) return;
        productList.remove(saleProduct);
    }

    /**
     * Maps visible column names to actual column names in the database. Used for sorting and filtering. Visible column
     * names are the ones that are exposed to the client. Actual column names are the ones in the database.
     */
    private static final Map<String, String> visibleColumnsToColumnNames = Map.of(
            "id", "id",
            "receivedMoney", "received_money",
            "createdAt", "created_at",
            "cashier", "cashier"
    );

    /**
     * Returns the visible column names that are exposed to the client.
     *
     * @return Set of visible column names
     */
    public static Set<String> getVisibleColumns() {
        return visibleColumnsToColumnNames.keySet();
    }

    /**
     * Returns the actual column name in the database by the given visible column name.
     *
     * @param visibleName visible column name. Usually comes from the client.
     * @return actual column name in the database
     */
    public static String getColumnName(String visibleName) {
        if (visibleName == null) return "id";
        return visibleColumnsToColumnNames.get(visibleName);
    }
}
