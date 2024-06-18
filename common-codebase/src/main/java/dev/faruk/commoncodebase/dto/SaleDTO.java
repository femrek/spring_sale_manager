package dev.faruk.commoncodebase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.entity.SaleProduct;
import lombok.*;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaleDTO {
    private Long id;
    private Double receivedMoney;
    private Timestamp createdAt;
    private UserDTO cashier;
    private List<SaleProductDTO> products;
    private List<OfferDTO> offers;

    public SaleDTO(Sale sale) {
        id = sale.getId();
        receivedMoney = sale.getReceivedMoney();
        cashier = new UserDTO(sale.getCashier());
        createdAt = sale.getCreatedAt();
        products = new ArrayList<>();
        for (SaleProduct m : sale.getProductList()) {
            products.add(new SaleProductDTO(m));
        }
        offers = new ArrayList<>();
        if (sale.getOffers() != null) {
            for (Offer o : sale.getOffers()) {
                offers.add(new OfferDTO(o));
            }
        }
    }

    public String getDate() {
        if (createdAt == null) return null;
        return createdAt.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getTime() {
        if (createdAt == null) return null;
        return createdAt.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getCashierName() {
        return cashier.getName();
    }

    public Double getChange() {
        return receivedMoney - getTotal();
    }

    public Double getTotal() {
        double total = products.stream().map(SaleProductDTO::getProductTotal).reduce(0.0, Double::sum);
        if (!offers.isEmpty()) {
            Double discount = offers.stream().map(OfferDTO::getDiscount).reduce(0.0, Double::sum);
            total *= (1.0D - discount);
        }
        return total;
    }
}
