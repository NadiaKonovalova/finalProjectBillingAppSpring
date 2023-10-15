package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerRepository;
import com.finalprojectbillingapp.user.UserRepository;
import com.finalprojectbillingapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceMenuController {
private UserService userService;
private UserRepository userRepository;
private CustomerRepository customerRepository;
@Autowired
public InvoiceMenuController(UserService userService, UserRepository userRepository){
    this.userService = userService;
    this.userRepository = userRepository;
}
    @GetMapping("createNewInvoice/customerData")
    public String createNewInvoiceWithCustomerData(){
        return "customerPageForInvoice";
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
/*

    // Parāda addCustomer lapu un Confirm vai Next;
    // ja izdodas, var uztaisīt vēlāk dropdown ar visiem klientiem
@GetMapping("createNewInvoice/customerData")
    return addCustomer; //

@PostMapping("createNewInvoice/customerData")
    // Parāda tālāk produktu/pakalpojumu lapu
    redirect addProductsOrServices; //

@GetMapping("createNewInvoice/productOrService")
// Parāda produktu/pakalpojumu lapu + Add more + confirm vai next pogu;
    // Ja izdodas, dropdown ar esošajiem produktiem
    return addProductsOrServices;
@PostMapping("createNewInvoice/productOrService")
    // parāda tālāk parakstu un piezīmju sadaļu
    redirect signatureAndNotes; // + datums; nav izveidots

    @GetMapping("createNewInvoice/signatureAndNotes")
    // pievieno paraksta veidu, datumu
    return signatureAndNotes;

    @PostMapping("createNewInvoice/signatureAndNotes")
// Confirm poga, novirza uz gatavo rēķinu;
            // Varētu būt confirm / save / edit opcijas
    redirect invoiceOverview;



@GetMapping
    @PostMapping ("/createNewInvoice")
    public void invoiceMenu(HttpServletRequest request) throws Exception {
        UserEntity user = this.userService.getLoggedInUser(request);
        if (user.getTaxpayerType().equals(Type.VAT_PAYER)) {
            // return chooseTaxPayerType; //
        } else {
            // code
            // Choose User
            return editUser;
            // Confirm
            // Select or add customer
            return addCustomer;
            // select or add product / service
            return addProductsorServices;
            // select or add notes, signature
            return addNotesAndSignature; //
            return invoiceOverview;

        }
    }*/
}
