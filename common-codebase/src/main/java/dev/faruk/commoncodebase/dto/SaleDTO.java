package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.entity.SaleProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private Double receivedMoney;
    private Timestamp createdAt;
    private UserDTO cashier;
    private List<SaleProductDTO> products;

    public SaleDTO(Sale sale) {
        id = sale.getId();
        receivedMoney = sale.getReceivedMoney();
        cashier = new UserDTO(sale.getCashier());
        products = new ArrayList<>();
        for (SaleProduct m:sale.getProductList()) {
            products.add(new SaleProductDTO(m));
        }
    }
}
