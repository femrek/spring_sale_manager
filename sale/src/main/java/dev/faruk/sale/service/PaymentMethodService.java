package dev.faruk.sale.service;

import dev.faruk.commoncodebase.dto.PaymentMethodDTO;
import dev.faruk.commoncodebase.repository.base.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
    private final SaleRepository saleRepository;

    public List<PaymentMethodDTO> getAll() {
        return saleRepository.findAllPaymentMethods().stream().map(PaymentMethodDTO::new).toList();
    }

    public PaymentMethodDTO getById(Long id) {
        return new PaymentMethodDTO(saleRepository.findPaymentMethodById(id));
    }
}
