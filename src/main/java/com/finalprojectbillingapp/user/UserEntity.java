package com.finalprojectbillingapp.user;

import com.finalprojectbillingapp.invoice.InvoiceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Entity (name="Users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserEntity {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();
   private String name;
    private String email;
    private String password;
    private String taxpayerNo;
    private String legalAddress;
    private String bankName;
    private String accountNo;
    @Enumerated(EnumType.STRING)
    private Type taxpayerType;
    @Enumerated (EnumType.STRING)
    private Country country;
    private Timestamp createdAt;
    private Timestamp lastUpdated;
    @OneToMany(mappedBy = "user")
    private List<InvoiceEntity> invoices;

    @PrePersist
    public void beforeSaveUser(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }

}

