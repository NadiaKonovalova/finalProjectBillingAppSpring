package com.finalprojectbillingapp.productOrService;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity (name="ProductOrService")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrServiceEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private double quantity;
    private String unit;
    private double unitPrice;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Category VATrate;

}
