package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    @GetMapping("/new-invoice")
    public String displayStartInvoicePage(){
       return "create_invoice";
   }
    /*   @GetMapping("/create_invoice")
     public String displayInvoicePage(HttpServletRequest request,Model model)throws Exception{
         // UserService userService = new UserService();
         UserEntity userEntity = new UserEntity();
         model.addAttribute("email", userEntity.getEmail());
         model.addAttribute("BankName", userEntity.getBankName());
         model.addAttribute("taxpayerNo", userEntity.getTaxpayerNo());
         model.addAttribute("legalAddress", userEntity.getLegalAddress());
         model.addAttribute("name", userEntity.getName());
         model.addAttribute("accountNo", userEntity.getAccountNo());
         return "create_invoice     } }*/
  //   @GetMapping("/create_invoice/invoiceNumber")
   //  public String displayInvoiceNumberPage(HttpSession session) {
    //     session.setAttribute("invoiceData", new InvoiceEntity());
   //      return "enterInvoiceNumber";
   //  }
    @PostMapping("/create_invoice")
    public String confirmInvoiceNumber(@RequestParam("invoiceNumber") String invoiceNumber,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) throws Exception {
        try {
            session.setAttribute("invoiceNumber", invoiceNumber);
            return "redirect:/create_invoice/userData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=INVOICE_NUMBER_FAILED&error=/";
        }
    }
    @GetMapping( "/create_invoice/userData")
    public String displayInvoiceUserPage (HttpServletRequest request,
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
  /*  @GetMapping("/createNewInvoice/taxPayerType")
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
    }*/
    @PostMapping("/creat_invoice/userData")
    public String confirmUserData(HttpServletRequest request, UserEntity user,
                                  RedirectAttributes redirectAttributes, Model model) throws Exception {

        user = this.userService.getLoggedInUser(request);
        UUID userId = user.getId();

        try {
            this.userService.editUserDetails(user, userId);
            this.userService.createUser(user);
            return "redirect:/create_invoice/customerData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/create_invoice/";
        }
    }
    @GetMapping("/create_invoice/customerData")
    public String displayInvoiceCustomerPage(Model model) {
        List<CustomerEntity> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customerPageForInvoice";

        }
        //return "allCustomers";
    @PostMapping("/create_invoice/customerData")
    public String addNewCustomerData(CustomerEntity customer,
                                     RedirectAttributes redirectAttributes,HttpServletRequest session) {
        try {
            this.customerService.createCustomer(customer);
            System.out.println(customer);
            return "redirect:/create_invoice/productOrService";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
//            return "redirect:/new-invoice/";
        }
    }
    @GetMapping("create_invoice/productOrService")
    public String testingDisplayInvoiceProductServicePage(HttpSession session) {
        List<ProductOrServiceEntity> allProducts = serviceForProducts.getAllProducts();
        session.setAttribute("products", allProducts);
        session.setAttribute("addMore",
                "createNewInvoice/productOrService/add-more");
   /*     // Does not show the confirmation button yet
        model.addAttribute("showConfirmationButton", false);*/
        return "addProductsOrServices";
    }
    // Vajadzetu tikt skaidrībā ar YES NO pogām un pirms Confirm parādīt visu produktu sarakstu
    @GetMapping("createNewInvoice/productOrService/add-more")
    public String testingAddMoreProductServicePage(HttpSession session) {
        List<ProductOrServiceEntity> existingProducts =
                (List<ProductOrServiceEntity>) session.getAttribute("products");
        return "addProductsOrServices";
    }
  /*  @PostMapping("createNewInvoice/productOrService")
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
    }*/
    @PostMapping("create_invoice/productOrService")
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
                return "redirect:/create_invoice/productOrService/add-more";
            } else  if ("No".equals(addMore)){
                return "redirect:/create_invoice/signatureAndNote";
            } else {
                return "redirect:/create_invoice/signatureAndNote";
            }
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }

    @GetMapping("/create_invoice/signatureAndNote")
    public String displaySignatureAndNotesPage(){
        return "signatureAndNote";
    }

    // NESAGLABĀ RĒĶINU
    @PostMapping("/create_invoice/signatureAndNote")
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

            return "redirect:/create_invoice/review-invoice";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=THIS_STAGE_FAILED&error=/";
        }
    }
    @GetMapping("/invoice-overview/{id}")
    public String displayInvoiceOverview(@PathVariable UUID id, Model model){
        try {
            InvoiceEntity invoice = invoiceService.getInvoiceById(id);
            model.addAttribute("invoice", invoice);
            UserEntity user = invoice.getUser();
            CustomerEntity customer = invoice.getCustomer();
            List<ProductOrServiceEntity> productsOrServices = invoice.getProducts();
            model.addAttribute("user", user);
            model.addAttribute("customer", customer);
            model.addAttribute("productsOrServices", productsOrServices);
            return "InvoiceOverview";
        } catch (Exception exception) {
            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
                    + exception.getMessage();
        }
    }
 //   @GetMapping("createNewInvoice/review-invoice")
   // public String displayInvoiceOverview(){
     //   return "testingInvoiceConfirm";
    //}

    public String displayInvoicesFromArchive(Model model){
        model.addAttribute("invoices",invoiceService.getAllInvoices());
        return "archiveInvoices";
    }

}