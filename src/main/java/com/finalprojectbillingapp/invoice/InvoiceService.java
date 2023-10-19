package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {
    InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    @Autowired
    public InvoiceService (InvoiceRepository invoiceRepository,
                           InvoiceProductRepository invoiceProductRepository){
        this.invoiceRepository=invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
    }

    public InvoiceEntity createNewInvoice(InvoiceEntity invoiceEntity) throws Exception{

        this.invoiceRepository.save(invoiceEntity);
        return invoiceEntity;
    }

    public List<InvoiceEntity>getAllInvoices(){
        return (ArrayList<InvoiceEntity>)
                this.invoiceRepository.findAll();
    }
    public InvoiceEntity getInvoiceById(UUID id) throws Exception{
        return this.invoiceRepository.findById(id).orElseThrow();
    }

    public List<InvoiceProductEntity> getAllInvoiceProducts(){
        return invoiceProductRepository.findAll();
    }

}