package dev.faruk.sale.datasource;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;

import java.util.List;

public class CashierData {
    public AppUser generateCashierUser(List<AppUserRole> roles) {
        return AppUser.builder()
                .username("cashier")
                .password("password")
                .name("Test User Cashier")
                .deleted(false)
                .roles(roles)
                .build();
    }
}
