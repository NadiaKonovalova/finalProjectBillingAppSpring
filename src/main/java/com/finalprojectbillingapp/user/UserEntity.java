package com.finalprojectbillingapp.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Entity (name="Users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserEntity {
    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
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

}

