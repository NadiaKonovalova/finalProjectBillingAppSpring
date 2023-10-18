package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity (name="Invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    private String invoiceNumber;
    private String notes;
    @Enumerated (EnumType.STRING)
    private Signature methodOfSigning;
    private Timestamp createdAt;
    private Timestamp lastUpdated;
    private Date issuedAt;
    private Date dueBy;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private CustomerEntity customer;
    @ManyToOne
    private ProductOrServiceEntity productOrService;
    @ManyToMany
    @JoinTable(
            name = "invoice_products",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductOrServiceEntity> products;

    @PrePersist
    public void beforeSaveInvoice(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }

}
