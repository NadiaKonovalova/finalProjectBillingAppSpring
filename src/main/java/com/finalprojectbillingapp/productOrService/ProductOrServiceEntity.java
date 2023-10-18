package com.finalprojectbillingapp.productOrService;

import com.finalprojectbillingapp.invoice.InvoiceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
    private Timestamp createdAt;
    private Timestamp lastUpdated;
    @ManyToOne
    InvoiceEntity invoice;

    @PrePersist
    public void beforeSaveProductService(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }

}