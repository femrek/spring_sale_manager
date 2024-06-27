package dev.faruk.commoncodebase.dto;

import dev.faruk.commoncodebase.entity.PaymentMethod;
import lombok.*;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentMethodDTO {
    private Long id;
    private String name;

    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        id = paymentMethod.getId();
        name = paymentMethod.getName();
    }
}
