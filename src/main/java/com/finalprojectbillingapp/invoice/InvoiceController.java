package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerRepository;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private InvoiceService invoiceService;
    private UserService userService;
    private CustomerService customerService;
    private ServiceForProducts serviceForProducts;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository,
                             UserService userService,
                             CustomerService customerService,
                             ServiceForProducts serviceForProducts) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.serviceForProducts = serviceForProducts;
    }
// Varbūt šo sadalīt atsevišķās metodēs: add user --> continue or edit; add, choose or edit customer;
    // add, choose or edit product
    // Manas izmaiņas

    @GetMapping("/new-invoice/")
    public String displayStartInvoicePage(){
      return "invoices";
    }


    @GetMapping("/createNewInvoice/taxPayerType")
    public String displayTaxpayerTypeSelection(HttpServletRequest request,
                                               Model model) throws Exception {
        UserEntity user = this.userService.getLoggedInUser(request);
        UUID userId = user.getId();
        try {
            if(userId != null) {
                model.addAttribute("user", user);
            }
            return "confirmTaxPayerType";
        } catch (Exception exception) {
            return "redirect:/?message=TAXPAYER_TYPE_CONFIRM_FAILED&error="
                    + exception.getMessage();
        }
    }
    @PostMapping("/createNewInvoice/taxPayerType")
    public String confirmTaxPayerType(HttpServletRequest request, UserEntity user,
                                      RedirectAttributes redirectAttributes) throws Exception {
        user = this.userService.getLoggedInUser(request);
        UUID userId = user.getId();

        try {
            this.userService.editTaxPayerType(user, userId);
            return "redirect:/createNewInvoice/userData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }

    @GetMapping("createNewInvoice/userData")
    public String displayInvoiceUserPage(HttpServletRequest request,
                                         Model model) throws Exception {
        UserEntity user = this.userService.getLoggedInUser(request);
        UUID userId = user.getId();
        try {
            if(userId != null) {
                model.addAttribute("user", user);
            }
            return "userPageForInvoice";
        } catch (Exception exception) {
            return "redirect:/?message=USER_CONFIRM_FAILED&error=";
        }
}

    @PostMapping("createNewInvoice/userData")
    public String confirmUserData(HttpServletRequest request, UserEntity user,
                                  RedirectAttributes redirectAttributes, Model model) throws Exception {

        user = this.userService.getLoggedInUser(request);
        UUID userId = user.getId();

        try {
            this.userService.editUserDetails(user, userId);
            this.userService.createUser(user);
            return "redirect:/createNewInvoice/customerData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }


 @GetMapping("createNewInvoice/customerData")
 public String displayInvoiceCustomerPage() {
   return "testAddCustomers";
 }

 @PostMapping("createNewInvoice/customerData")
 public String addNewCustomerData(CustomerEntity customer,
                               RedirectAttributes redirectAttributes) {
        try {
            this.customerService.createCustomer(customer);
            System.out.println(customer);
            return "redirect:/createNewInvoice/productOrService";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
//            return "redirect:/new-invoice/";
        }
    }

    @GetMapping("createNewInvoice/productOrService")
    public String displayInvoiceProductServicePage(Model model){
        model.addAttribute("addMore",
                "createNewInvoice/productOrService/add-more");
   /*     // Does not show the confirmation button yet
        model.addAttribute("showConfirmationButton", false);*/
        return "testingAddProduct";
    }

// Vajadzetu tikt skaidrībā ar YES NO pogām un pirms Confirm parādīt visu produktu sarakstu
    @GetMapping("createNewInvoice/productOrService/add-more")
    public String addMoreProductServicePage(){
        return "testingAddProduct";
    }

    @PostMapping("createNewInvoice/productOrService")
    public String confirmInvoiceProductServiceData(ProductOrServiceEntity productOrService,
                                                   RedirectAttributes redirectAttributes,
                                                   @RequestParam("addMore") boolean addMore,
                                                    Model model) {
        try {
            this.serviceForProducts.createProductService(productOrService);
            if (addMore){
                return "testingAddProduct";
            } else {
//                model.addAttribute("showConfirmationButton", true);
                return "redirect:/createNewInvoice/signatureAndNotes";
            }
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }

    @GetMapping("createNewInvoice/signatureAndNotes")
    public String displaySignatureAndNotesPage(){
        return "signatureAndNotes";
    }

    @PostMapping("createNewInvoice/signatureAndNotes")
    
//
//    @PostMapping("createNewInvoice/signatureAndNotes")
//
//    @GetMapping("/archive-invoice")*/
    public String displayInvoicesFromArchive(Model model){
        model.addAttribute("invoices",invoiceService.getAllInvoices());
        return "archiveInvoices";
    }




/*    @GetMapping("/new-invoice/")
    public String displayInvoicePage(HttpServletRequest request,
                                     Model model)throws Exception{
        // UserService userService = new UserService();

        UserEntity userEntity = this.userService.getLoggedInUser(request);
        this.userService.editUserDetails(userEntity, userEntity.getId());
        model.addAttribute("user", userEntity);
        model.addAttribute("name", userEntity.getName());
        model.addAttribute("taxpayerNo", userEntity.getTaxpayerNo());
        model.addAttribute("legalAddress", userEntity.getLegalAddress());
        model.addAttribute("country", userEntity.getCountry().getDisplayName());
        model.addAttribute("BankName", userEntity.getBankName());
        model.addAttribute("accountNo", userEntity.getAccountNo());
        model.addAttribute("email", userEntity.getEmail());

        return "create_invoice";
    }*/


    //  @PostMapping("/archive-invoice")


}