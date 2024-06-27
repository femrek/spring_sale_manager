package dev.faruk.commoncodebase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_method", schema = "public")
public class PaymentMethod {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "method_name")
    private String name;
}
