package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
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

    @Query("SELECT i.customer FROM Invoices i WHERE i.user.loginEmail = :loginEmail")
    List<CustomerEntity> findCustomersByUserLoginEmail(String loginEmail);

    @Query("SELECT i FROM Invoices i ORDER BY i.createdAt")
    List<InvoiceEntity> sortAllByCreatedAt();

    @Query("SELECT i FROM Invoices i ORDER BY i.issuedAt")
    List<InvoiceEntity> sortAllByIssuedAt();

    @Query("SELECT i FROM Invoices i ORDER BY i.dueBy")
    List<InvoiceEntity> sortAllByDueBy();

    @Query("SELECT i FROM Invoices i ORDER BY i.user.name")
    List<InvoiceEntity> sortAllByUserName();

    @Query("SELECT i FROM Invoices i ORDER BY i.customer.name")
    List<InvoiceEntity> sortAllByCustomerName();


    @Query("SELECT i FROM Invoices i ORDER BY i.totalPrice")
    List<InvoiceEntity> sortAllByTotal();

    @Query("SELECT i FROM Invoices i ORDER BY i.currency")
    List<InvoiceEntity> sortAllByCurrency();

    @Query("SELECT i FROM Invoices i ORDER BY i.status")
    List<InvoiceEntity> sortAllByStatus();

    @Query("SELECT DISTINCT i FROM Invoices i JOIN FETCH i.user")
    List<InvoiceEntity> findAllWithUsers();


}
