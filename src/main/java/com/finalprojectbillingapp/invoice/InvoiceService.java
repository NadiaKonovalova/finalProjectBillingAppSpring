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

    @Autowired
    public InvoiceService (InvoiceRepository invoiceRepository){
        this.invoiceRepository=invoiceRepository;
    }

    public void createNewInvoice(InvoiceEntity invoiceEntity) throws Exception{

        this.invoiceRepository.save(invoiceEntity);
    }

    public List<InvoiceEntity>getAllInvoices(){
        return (ArrayList<InvoiceEntity>)
                this.invoiceRepository.findAll();
    }
    public InvoiceEntity getInvoiceById(UUID id) throws Exception{
        return this.invoiceRepository.findById(id).orElseThrow();
    }

}