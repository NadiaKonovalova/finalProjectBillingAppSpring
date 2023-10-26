package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity(name="productsInInvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductOrServiceEntity product;
    @ManyToOne
    private InvoiceEntity invoice;



}
