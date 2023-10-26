package com.finalprojectbillingapp.productOrService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductServiceRepository extends
        CrudRepository
                <ProductOrServiceEntity, UUID> {
    ProductOrServiceEntity findByIdOrName
            (UUID id, String name);

}
