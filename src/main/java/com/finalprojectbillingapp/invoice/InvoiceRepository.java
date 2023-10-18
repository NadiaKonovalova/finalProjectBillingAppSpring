package com.finalprojectbillingapp.invoice;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceRepository extends CrudRepository
        <InvoiceEntity, UUID> {
    InvoiceEntity findByInvoiceNumber
            (String invoiceNumber);
}