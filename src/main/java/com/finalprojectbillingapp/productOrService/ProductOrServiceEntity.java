package com.finalprojectbillingapp.productOrService;

import com.finalprojectbillingapp.invoice.InvoiceProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity (name="ProductOrService")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "invoices")
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
    private double totalPerProduct;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<InvoiceProductEntity> invoices = new ArrayList<>();

    @PrePersist
    public void beforeSaveProductService(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }

}