package com.finalprojectbillingapp.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {
    InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository){
        this.invoiceRepository=invoiceRepository;
    }

    public void createNewInvoice(InvoiceEntity invoiceEntity) throws Exception{

        this.invoiceRepository.save(invoiceEntity);
    }

    public void createInvoice (InvoiceEntity invoiceEntity)
            throws Exception {
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