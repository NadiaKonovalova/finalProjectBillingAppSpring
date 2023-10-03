package com.finalprojectbillingapp.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller

public class InvoiceController {
        private final InvoiceRepository invoiceRepository;

        @Autowired
        public InvoiceController(InvoiceRepository invoiceRepository) {
            this.invoiceRepository = invoiceRepository;
        }
}
