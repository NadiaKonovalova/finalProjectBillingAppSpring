package com.finalprojectbillingapp.productOrService;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductServiceRepository extends
        CrudRepository
                <ProductOrServiceEntity, UUID> {
    ProductOrServiceEntity findByIdOrName
            (UUID id, String name);
}