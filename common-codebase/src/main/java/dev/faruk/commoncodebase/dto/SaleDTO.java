package dev.faruk.commoncodebase.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.faruk.commoncodebase.entity.Offer;
import dev.faruk.commoncodebase.entity.OfferProduct;
import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.entity.SaleProduct;
import dev.faruk.commoncodebase.error.AppHttpError;
import lombok.*;
import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class SaleDTO {
    private Long id;
    private Double receivedMoney;
    private Timestamp createdAt;
    private PaymentMethodDTO paymentMethod;
    private UserDTO cashier;
    private List<SaleProductDTO> products;
    private List<OfferDTO> offers;

    public SaleDTO(Sale sale) {
        id = sale.getId();
        receivedMoney = sale.getReceivedMoney();
        createdAt = sale.getCreatedAt();
        paymentMethod = sale.getPaymentMethod() == null ? null : new PaymentMethodDTO(sale.getPaymentMethod());
        cashier = new UserDTO(sale.getCashier());
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

    @JsonIgnore
    public String getPaymentMethodName_tr() {
        if (paymentMethod == null) return null;
        if (paymentMethod.getName() == null) return null;
        if (paymentMethod.getName().equals("CASH")) return "Nakit";
        if (paymentMethod.getName().equals("CREDIT_CARD")) return "Kredi Kartı";
        throw new AppHttpError.InternalServerError("Unknown payment method");
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
            final Double discount = offers.stream().map(OfferDTO::getDiscount).reduce(0.0, Double::sum);
            total -= discount;
        }
        return total;
    }
}
