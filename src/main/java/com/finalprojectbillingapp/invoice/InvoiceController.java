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

        @GetMapping("")
        public String displayHomePage(){
            return "index";
        }
        @GetMapping("/about")
        public String aboutPage(){
            return "about";
        }

}
