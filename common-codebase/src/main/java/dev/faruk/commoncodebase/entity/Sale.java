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
@Table(name = "sale", schema = "public")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "received_money")
    private double receivedMoney;

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

    public Sale(double receivedMoney, AppUser cashier, List<SaleProduct> productList) {
        this.receivedMoney = receivedMoney;
        this.cashier = cashier;
        this.productList = productList;
    }

    public Sale(double receivedMoney, AppUser cashier) {
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
}
