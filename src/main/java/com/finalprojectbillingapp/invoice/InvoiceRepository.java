package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends CrudRepository
        <InvoiceEntity, UUID> {
    InvoiceEntity findByInvoiceNumber
            (String invoiceNumber);

    @Query("SELECT i FROM Invoices i WHERE i.user.loginEmail = :loginEmail")
    List<InvoiceEntity> findInvoicesByUserLoginEmail(String loginEmail);

}
