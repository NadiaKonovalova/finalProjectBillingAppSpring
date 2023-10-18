package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private InvoiceService invoiceService;
    private UserService userService;
    private CustomerService customerService;
    private ServiceForProducts serviceForProducts;

        @ModelAttribute("invoiceData")
        public InvoiceEntity collectInvoiceData() {
            return new InvoiceEntity();
    }
    @PersistenceContext
    private EntityManager entityManager;
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

// Strādā
    @GetMapping("/new-invoice/")
    public String displayStartInvoicePage(){
      return "invoices";
    }

// Strādā
    @GetMapping("/createNewInvoice/invoiceNumber")
    public String displayInvoiceNumberPage(HttpSession session) {
        session.setAttribute("invoiceData", new InvoiceEntity());
        return "enterInvoiceNumber";
    }
    @PostMapping("/createNewInvoice/confirmInvoiceNumber")
    public String confirmInvoiceNumber(@RequestParam("invoiceNumber") String invoiceNumber,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) throws Exception {
        try {
            session.setAttribute("invoiceNumber", invoiceNumber);
            return "redirect:/createNewInvoice/userData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=INVOICE_NUMBER_FAILED&error=/";
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
    public String confirmUserData(@ModelAttribute("user") UserEntity user,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) throws Exception {

        UserEntity loggedInUser = this.userService.getLoggedInUser(request);
        UUID userId = loggedInUser.getId();

        try {
            this.userService.editUserDetails(loggedInUser, userId);
            this.userService.createUser(user);
            session.setAttribute("user", user);
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
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        try {
            this.customerService.createCustomer(customer);
            session.setAttribute("customer", customer);
            return "redirect:/createNewInvoice/productOrService";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
//            return "redirect:/new-invoice/";
        }
    }

   /* @GetMapping("createNewInvoice/productOrService")
    public String displayInvoiceProductServicePage(Model model){
        model.addAttribute("addMore",
                "createNewInvoice/productOrService/add-more");
   *//*     // Does not show the confirmation button yet
        model.addAttribute("showConfirmationButton", false);*//*
        return "testingAddProduct";*/
//    }

    @GetMapping("createNewInvoice/productOrService")
    public String testingDisplayInvoiceProductServicePage(HttpSession session){
        List<ProductOrServiceEntity> allProducts = serviceForProducts.getAllProducts();
        session.setAttribute("products", allProducts);
        session.setAttribute("addMore",
                "createNewInvoice/productOrService/add-more");
   /*     // Does not show the confirmation button yet
        model.addAttribute("showConfirmationButton", false);*/
        return "testingAddProduct";
    }

    @GetMapping("createNewInvoice/productOrService/add-more")
    public String testingAddMoreProductServicePage(HttpSession session) {
        List<ProductOrServiceEntity> existingProducts =
                (List<ProductOrServiceEntity>) session.getAttribute("products");

        return "testingAddProduct";
    }
// Vajadzetu tikt skaidrībā ar YES NO pogām un pirms Confirm parādīt visu produktu sarakstu
  /*  @GetMapping("createNewInvoice/productOrService/add-more")
    public String addMoreProductServicePage(){
        return "testingAddProduct";
    }*/

    // NESAGLABĀ BEZ ADD MORE
/*    @PostMapping("createNewInvoice/productOrService")
    public String confirmInvoiceProductServiceData(ProductOrServiceEntity productOrService,
                                                   RedirectAttributes redirectAttributes,
                                                   @RequestParam("addMore") boolean addMore,
                                                   HttpSession session) {
        List<ProductOrServiceEntity> productsOrServices = (List<ProductOrServiceEntity>) session.getAttribute("items");
        if (productsOrServices == null) {
            productsOrServices = new ArrayList<>();
        }
        try {
            this.serviceForProducts.createProductService(productOrService);
            productsOrServices.add(productOrService);
            session.setAttribute("item", productOrService);
            if (!addMore){
                return "redirect:/createNewInvoice/signatureAndNotes";
            } else {
                this.serviceForProducts.createProductService(productOrService);
                return "testingAddProduct";
            }
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }*/

    @PostMapping("createNewInvoice/productOrService")
    public String testingConfirmInvoiceProductServiceData2(
            @RequestParam(value = "existingProductIds", required = false) List<UUID> existingProductIds,
            @ModelAttribute("newProduct") ProductOrServiceEntity newProduct,
            RedirectAttributes redirectAttributes,
            @RequestParam("addMore") String addMore,
            HttpSession session) {
        try {
            existingProductIds = this.serviceForProducts.getAllProductOrServiceIds();
            List<ProductOrServiceEntity> selectedProducts = new ArrayList<>();
            session.setAttribute("products", selectedProducts);
            if (existingProductIds != null) {
                List<ProductOrServiceEntity> allProducts = (List<ProductOrServiceEntity>) session.getAttribute("products");
                for (UUID productId : existingProductIds) {
                    for (ProductOrServiceEntity product : allProducts) {
                        if (product.getId().equals(productId)) {
                            selectedProducts.add(product);
                            break;
                        }
                    }
                }
            }
            if (newProduct != null) {
                serviceForProducts.createProductService(newProduct);
                selectedProducts.add(newProduct);
                System.out.println(selectedProducts);
            }
            if ("Yes".equals(addMore)) {
                this.testingDisplayInvoiceProductServicePage(session);
                return "redirect:/createNewInvoice/productOrService/add-more";
            } else  if ("No".equals(addMore)){
                return "redirect:/createNewInvoice/signatureAndNotes";
            } else {
                return "redirect:/createNewInvoice/signatureAndNotes";
            }
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }


    @PostMapping("createNewInvoice/productOrService")
    public String testingConfirmInvoiceProductServiceData(
            @RequestParam(value = "existingProductIds", required = false) List<UUID> existingProductIds,
            @ModelAttribute("newProduct") ProductOrServiceEntity newProduct,
            RedirectAttributes redirectAttributes,
            @RequestParam("addMore") String addMore,
            HttpSession session) {
        try {
            existingProductIds = this.serviceForProducts.getAllProductOrServiceIds();
            List<ProductOrServiceEntity> selectedProducts = new ArrayList<>();
            session.setAttribute("products", selectedProducts);
            if (existingProductIds != null) {
                List<ProductOrServiceEntity> allProducts = (List<ProductOrServiceEntity>) session.getAttribute("products");
                for (UUID productId : existingProductIds) {
                    for (ProductOrServiceEntity product : allProducts) {
                        if (product.getId().equals(productId)) {
                            selectedProducts.add(product);
                            break;
                        }
                    }
                }
            }
            if (newProduct != null) {
                serviceForProducts.createProductService(newProduct);
                selectedProducts.add(newProduct);
                System.out.println(selectedProducts);
            }
            if ("Yes".equals(addMore)) {
                this.testingDisplayInvoiceProductServicePage(session);
                return "redirect:/createNewInvoice/productOrService/add-more";
            } else  if ("No".equals(addMore)){
                return "redirect:/createNewInvoice/signatureAndNotes";
            } else {
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

    // NESAGLABĀ RĒĶINU
    @PostMapping("createNewInvoice/signatureAndNotes")
    public String testingConfirmSignatureAndNotesPage(@RequestParam("notes") String notes,
                                               @RequestParam("methodOfSigning") Signature methodOfSigning,
                                               @RequestParam("issuedAt") Date issuedAt,
                                               @RequestParam("dueBy") Date dueBy,
                                               RedirectAttributes redirectAttributes,
                                               HttpSession session){
        try {
            session.setAttribute("notes", notes);
            session.setAttribute("methodOfSigning", methodOfSigning);
            session.setAttribute("issuedAt", issuedAt);
            session.setAttribute("dueBy", dueBy);

            String invoiceNumber = (String) session.getAttribute("invoiceNumber");
            UserEntity user = (UserEntity) session.getAttribute("user");
            CustomerEntity customer = (CustomerEntity) session.getAttribute("customer");

            List<UUID> productIDs = this.serviceForProducts.getAllProductOrServiceIds();

            List<ProductOrServiceEntity> allProducts =
                    this.serviceForProducts.getAllProducts();
            List<ProductOrServiceEntity> selectedProducts = new ArrayList<>();


            if (productIDs != null) {
                for (UUID productId : productIDs) {
                    for (ProductOrServiceEntity product : allProducts) {
                        if (product.getId().equals(productId)) {
                            selectedProducts.add(product);
                            break;
                        }
                    }
                }
            }
            session.setAttribute("products", selectedProducts);

            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setNotes(notes);
            invoice.setMethodOfSigning(methodOfSigning);
            invoice.setIssuedAt(issuedAt);
            invoice.setDueBy(dueBy);
            invoice.setUser(user);
            invoice.setCustomer(customer);
            invoice.setProducts(selectedProducts);

            this.invoiceService.createNewInvoice(invoice);
            System.out.println(invoice);
            entityManager.flush();

            return "redirect:/createNewInvoice/review-invoice";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=THIS_STAGE_FAILED&error=/";
        }
    }

   /* @PostMapping("createNewInvoice/generate-review")
    public String generateInvoiceToReview(HttpSession session, Model model,
                                          RedirectAttributes redirectAttributes) throws Exception {
        try {
            String invoiceNumber = (String) session.getAttribute("invoiceNumber");
            String notes = (String) session.getAttribute("notes");
            Signature methodOfSigning = (Signature) session.getAttribute("signature");
            Date issuedAt = (Date) session.getAttribute("issuedAt");
            Date dueBy = (Date) session.getAttribute("dueBy");
            UserEntity user = (UserEntity) session.getAttribute("user");
            CustomerEntity customer = (CustomerEntity) session.getAttribute("customer");
            ProductOrServiceEntity productOrService =
                    (ProductOrServiceEntity) session.getAttribute("item");

            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setNotes(notes);
            invoice.setMethodOfSigning(methodOfSigning);
            invoice.setIssuedAt(issuedAt);
            invoice.setDueBy(dueBy);
            invoice.setUser(user);
            invoice.setCustomer(customer);
            invoice.setProductOrService(productOrService);

            this.invoiceService.createNewInvoice(invoice);
            entityManager.flush();
            return "redirect:/createNewInvoice/review-invoice";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=GENERATE_FOR_REVIEW_FAILED&error=/";
        }
    }*/

    // REMOVE SESSION ATTRIBUTES IN FINAL CONFIRM
    // TESTING TESTING TESTING
    @GetMapping("createNewInvoice/review-invoice")
    public String displayInvoiceOverview(){
        return "testingInvoiceConfirm";
    }

//    @GetMapping("/invoice-overview/{id}")
//    public String displayInvoiceOverview(@PathVariable UUID id, Model model){
//        try {
//            InvoiceEntity invoice = invoiceService.getInvoiceById(id);
//            model.addAttribute("invoice", invoice);
//            UserEntity user = invoice.getUser();
//            CustomerEntity customer = invoice.getCustomer();
//            ProductOrServiceEntity productsOrServices = invoice.getProductOrService();
//
//            model.addAttribute("user", user);
//            model.addAttribute("customer", customer);
//            model.addAttribute("productsOrServices", productsOrServices);
//
//            // TESTING TESTING TESTING
//            return "testingInvoiceConfirm";
//        } catch (Exception exception) {
//            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
//                    + exception.getMessage();
//        }
//    }

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