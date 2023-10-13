package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.user.UserEntity;
import com.finalprojectbillingapp.user.CookieHandling;
import com.finalprojectbillingapp.user.UserRepository;
import com.finalprojectbillingapp.user.UserService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private InvoiceService invoiceService;
    private UserService userService;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository,
                             UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }
// Varbūt šo sadalīt atsevišķās metodēs: add user --> continue or edit; add, choose or edit customer;
    // add, choose or edit product
    @GetMapping("/new-invoice")
    public String displayInvoicePage(HttpServletRequest request,
                                     Model model)throws Exception{
        // UserService userService = new UserService();

        UserEntity userEntity = this.userService.getLoggedInUser(request);
        model.addAttribute("email", userEntity.getEmail());
        model.addAttribute("BankName", userEntity.getBankName());
        model.addAttribute("taxpayerNo", userEntity.getTaxpayerNo());
        model.addAttribute("legalAddress", userEntity.getLegalAddress());
        model.addAttribute("country", userEntity.getCountry().getDisplayName());
        model.addAttribute("name", userEntity.getName());
        model.addAttribute("accountNo", userEntity.getAccountNo());
        return "create_invoice";
    }

    @GetMapping("/archive-invoice")
    public String displayInvoicesFromArchive(Model model){
        model.addAttribute("invoices",invoiceService.getAllInvoices());
        return "archiveInvoices";
    }
    //  @PostMapping("/archive-invoice")


}