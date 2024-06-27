package dev.faruk.sale.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.PaymentMethodDTO;
import dev.faruk.sale.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment-method")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping({"/", ""})
    public ResponseEntity<AppSuccessResponse<List<PaymentMethodDTO>>> getAll() {
        return new AppSuccessResponse<>(
                "payment methods retrieved successfully",
                paymentMethodService.getAll()).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppSuccessResponse<PaymentMethodDTO>> getById(@PathVariable Long id) {
        return new AppSuccessResponse<>(
                "payment method retrieved successfully",
                paymentMethodService.getById(id)).toResponseEntity();
    }
}
