package com.finalprojectbillingapp.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CustomerRepository extends CrudRepository
        <CustomerEntity, UUID> {
    CustomerEntity findByTaxpayerNoOrName
            (String taxpayerNo, String name);
}
