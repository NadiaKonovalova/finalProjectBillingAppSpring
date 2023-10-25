package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity (name="Invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "invoiceProducts")
public class InvoiceEntity {
    @Id @GeneratedValue (strategy = GenerationType.AUTO)
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
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceProductEntity> invoiceProducts = new ArrayList<>();
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;
    private Currency currency;

    @PrePersist
    public void beforeSaveInvoice(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }
}
