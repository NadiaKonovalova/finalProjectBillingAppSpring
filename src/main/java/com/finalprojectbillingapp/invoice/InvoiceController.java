package com.finalprojectbillingapp.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class InvoiceController {
        private final InvoiceRepository invoiceRepository;

        @Autowired
        public InvoiceController(InvoiceRepository invoiceRepository) {
            this.invoiceRepository = invoiceRepository;
        }

    @GetMapping("/new-invoice")
    public String displayInvoicePage(){
        return "create_invoice";
    }
    @GetMapping("/archive-invoice")
    public String displayInvoicesFromArchive(){
        return "archiveInvoices";
    }
}
