package com.finalprojectbillingapp.customer;

import com.finalprojectbillingapp.user.Country;
import com.finalprojectbillingapp.user.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity (name = "Customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();
    private String name;
    private String taxpayerNo;
    private String legalAddress;
    private String bankName;
    private String accountNo;
    @Enumerated (EnumType.STRING)
    private Type taxpayerType;
    @Enumerated (EnumType.STRING)
    private Country country;
    private Timestamp createdAt;
    private Timestamp lastUpdated;

    @PrePersist
    public void beforeSaveCustomer (){
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }
}
