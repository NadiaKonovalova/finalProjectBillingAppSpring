package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface InvoiceProductRepository extends CrudRepository<InvoiceProductEntity, UUID> {
    List<InvoiceProductEntity> findAll();
    /*  InvoiceProductEntity getInvoiceProductEntityByInvoice
              (InvoiceEntity invoice);
      InvoiceProductEntity getInvoiceProductEntitiesByProduct
              (ProductOrServiceEntity product);*/
    List<ProductOrServiceEntity> findAllByInvoice(InvoiceEntity invoice);

    /*    InvoiceProductEntity findAllByInvoiceId(UUID id);*/



    /*    @Query("SELECT ip.product FROM productsInInvoice ip WHERE ip.invoice = :invoiceId")*/
}