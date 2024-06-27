package dev.faruk.commoncodebase.repository.testImplementations;

import dev.faruk.commoncodebase.entity.PaymentMethod;
import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SaleRepositoryTestImpl implements SaleRepository {
    private final List<Sale> cache = new ArrayList<>();
    private long idCounter = 1;

    private final List<PaymentMethod> paymentMethods = List.of(
            new PaymentMethod(1L, "CASH"),
            new PaymentMethod(2L, "CREDIT_CARD")
    );

    @Override
    public Sale create(Sale sale) {
        sale.setId(idCounter++);
        cache.add(sale);
        return sale;
    }

    @Override
    public List<Sale> findAll(Integer page,
                              Integer size,
                              String orderBy,
                              Boolean orderAsc,
                              Long dateFilterAfter,
                              Long dateFilterBefore,
                              Long cashierFilterId,
                              Double receivedMoneyFilterMin,
                              Double receivedMoneyFilterMax) {
        List<Sale> result = getSales(orderBy, orderAsc);

        // filter
        if (dateFilterAfter != null) {
            result.removeIf(sale -> sale.getCreatedAt().toInstant().toEpochMilli() < dateFilterAfter);
        }
        if (dateFilterBefore != null) {
            result.removeIf(sale -> sale.getCreatedAt().toInstant().toEpochMilli() > dateFilterBefore);
        }
        if (cashierFilterId != null) {
            result.removeIf(sale -> !sale.getCashier().getId().equals(cashierFilterId));
        }
        if (receivedMoneyFilterMin != null) {
            result.removeIf(sale -> sale.getReceivedMoney() < receivedMoneyFilterMin);
        }
        if (receivedMoneyFilterMax != null) {
            result.removeIf(sale -> sale.getReceivedMoney() > receivedMoneyFilterMax);
        }

        // pagination if needed
        if (page == null && size == null) return result;
        if (page == null || size == null) {
            throw new IllegalArgumentException("Page and size are required");
        }
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be greater than 0");
        }
        final int start = (page - 1) * size;
        final int end = Math.min(start + size, result.size());

        if (start >= result.size()) return List.of();
        return result.subList(start, end);
    }

    private List<Sale> getSales(String orderBy, Boolean orderAsc) {
        final String finalOrderBy = orderBy == null ? "id" : orderBy;

        List<Sale> result = new ArrayList<>(cache);
        if (orderAsc == null || orderAsc) {
            result.sort((s1, s2) -> switch (finalOrderBy) {
                case "id" -> s1.getId().compareTo(s2.getId());
                case "createdAt" -> s1.getCreatedAt().compareTo(s2.getCreatedAt());
                case "cashier" -> s1.getCashier().getName().compareTo(s2.getCashier().getName());
                case "receivedMoney" -> s1.getReceivedMoney().compareTo(s2.getReceivedMoney());
                default -> throw new IllegalArgumentException(
                        "Invalid orderBy parameter: %s. orderBy must be one of them: id, createdAt, cashier, receivedMoney".formatted(orderBy));
            });
        } else {
            result.sort((s1, s2) -> switch (finalOrderBy) {
                case "id" -> s2.getId().compareTo(s1.getId());
                case "createdAt" -> s2.getCreatedAt().compareTo(s1.getCreatedAt());
                case "cashier" -> s2.getCashier().getName().compareTo(s1.getCashier().getName());
                case "receivedMoney" -> s2.getReceivedMoney().compareTo(s1.getReceivedMoney());
                default -> throw new IllegalArgumentException(
                        "Invalid orderBy parameter: %s. orderBy must be one of them: id, createdAt, cashier, receivedMoney".formatted(orderBy));
            });
        }

        return result;
    }

    @Override
    public Sale findById(Long id) {
        return cache.stream().filter(sale -> sale.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deletePermanent(Sale sale) {
        cache.remove(sale);
    }

    @Override
    public List<PaymentMethod> findAllPaymentMethods() {
        return paymentMethods;
    }

    @Override
    public PaymentMethod findPaymentMethodById(Long id) {
        return paymentMethods.stream().filter(pm -> pm.getId().equals(id)).findFirst().orElse(null);
    }
}
