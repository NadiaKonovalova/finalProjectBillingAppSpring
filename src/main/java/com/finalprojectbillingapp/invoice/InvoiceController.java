package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerRepository;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.UserEntity;
import com.finalprojectbillingapp.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private InvoiceService invoiceService;
    private UserEntity userEntity;
    private UserService userService;
    private CustomerService customerService;
    private CustomerEntity customerEntity;
    private ServiceForProducts serviceForProducts;
    private ProductOrServiceEntity productOrServiceEntity;
    private CustomerRepository customerRepository;
    InvoiceEntity invoiceEntity;
    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository,
                             UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }
    @GetMapping("/new-invoice/")
    public String displayStartInvoicePage(){
        return "create_invoice";
    }
    @GetMapping("createNewInvoice")
    public String startActionForCreatingInvoice(){
        return "confirmTaxPayerType";
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
            return "redirect:/new-invoice";
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
            return "redirect:/createNewInvoice/customerData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice";
        }
    }


  /*  @GetMapping("createNewInvoice/customerData")
        public String createNewInvoiceWithCustomerData(){
       return "/addCustomer";
    }
    @PostMapping("createNewInvoice/customerData")
    public String addNewCustomerForInvoice(CustomerEntity customerEntity){
        try{
            CustomerEntity savedCustomer = customerRepository.save(customerEntity);
            String customerId = String.valueOf(savedCustomer.getId());
            return "redirect:/addProductsOrServices";
        }catch (Exception e){
            return "redirect:/customer_additing_FAILED&error" + e.getMessage();
        }
    }
*/
    @GetMapping("/new-invoice")
    public String displayInvoicePage(@PathVariable UUID id,HttpServletRequest request,Model model) throws Exception {
        return "invoices";
    }
    @PostMapping ("/new-invoice")
    public String createInvoice(Model model){
        try {
            this.invoiceService.createInvoice(invoiceEntity);
            return "redirect:/invoice?status=CREATION_SUCCESS";
        } catch (Exception exception) {
            return "redirect:/invoice?status=CREATION_FAILED&?error="
                    + exception.getMessage();
        }
       /// this.customerForInvoice(id,model);
     //   this.userForInvoice(request,model);
    //    this.productForInvoice(id,model);
    //    return "create_invoice";
        //try {
        //   } catch (Exception exception) {
        //     return "redirect:/?message=invoice_creating_FAILED&error=" + exception.getMessage();
    }

  /*  private void userForInvoice(HttpServletRequest request, Model model) throws Exception{
        UserEntity userEntity = this.userService.getLoggedInUser(request);
        model.addAttribute("name", this.userEntity.getName());
        model.addAttribute("email", this.userEntity.getEmail());
        model.addAttribute("legalAddress", this.userEntity.getLegalAddress());
        model.addAttribute("BankName", this.userEntity.getBankName());
        //model.addAttribute("taxpayerNo", this.userEntity.getTaxpayerNo());
        model.addAttribute("accountNo", this.userEntity.getAccountNo());
    }
    public String customerForInvoice(@PathVariable UUID id, Model model) throws Exception{
        CustomerEntity customerEntity = this.customerService.findCustomerById(id);
        model.addAttribute("name", this.customerEntity.getName());
        model.addAttribute("taxpayerNo", this.customerEntity.getName());
        model.addAttribute("legalAddress", this.customerEntity.getLegalAddress());
        model.addAttribute("BankName", this.customerEntity.getBankName());
        //model.addAttribute("taxpayerNo", this.userEntity.getTaxpayerNo());
        model.addAttribute("accountNo", this.customerEntity.getAccountNo());
        return null;
    }
    private void productForInvoice(@PathVariable UUID id, Model model)throws Exception {
        ProductOrServiceEntity productOrServiceEntity = this.serviceForProducts.findProductOrServiceById(id);
        model.addAttribute("name",this.productOrServiceEntity.getName());
        model.addAttribute("quantity", this.productOrServiceEntity.getQuantity());
        model.addAttribute("unit", this.productOrServiceEntity.getUnit());
        model.addAttribute("currency",this.productOrServiceEntity.getCurrency());
        model.addAttribute("VATrate",this.productOrServiceEntity.getVATrate());
    }


    @GetMapping("/archive-invoice")
    public String displayInvoicesFromArchive(Model model){
        model.addAttribute("invoices",invoiceService.getAllInvoices());
        return "archiveInvoices";
    }
    //  @PostMapping("/archive-invoice") */


}