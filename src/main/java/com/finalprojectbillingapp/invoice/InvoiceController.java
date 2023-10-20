package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerRepository;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.Category;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ProductServiceRepository;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
import jakarta.persistence.ManyToOne;
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
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final ProductServiceRepository productServiceRepository;
    private InvoiceService invoiceService;
    private UserService userService;
    private CustomerService customerService;
    private ServiceForProducts serviceForProducts;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository,
                             UserService userService,
                             CustomerService customerService,
                             ServiceForProducts serviceForProducts,
                             ProductServiceRepository productServiceRepository,
                             InvoiceService invoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.serviceForProducts = serviceForProducts;
        this.productServiceRepository = productServiceRepository;
        this.invoiceService = invoiceService;
    }

    // Works
    @GetMapping("/new-invoice/")
    public String displayStartInvoicePage() {
        return "invoices";
    }

    // Works
    @GetMapping("/createNewInvoice/invoiceNumber")
    public String displayInvoiceNumberPage(HttpSession session) {
        session.setAttribute("invoiceData", new InvoiceEntity());
        return "enterInvoiceNumber";
    }

    // Works
    @PostMapping("/createNewInvoice/confirmInvoiceNumber")
    public String confirmInvoiceNumber(@RequestParam("invoiceNumber") String invoiceNumber,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) throws Exception {
        try {
            session.setAttribute("invoiceNumber", invoiceNumber);
            System.out.println("Invoice number: " + invoiceNumber);
            return "redirect:/createNewInvoice/userData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=INVOICE_NUMBER_FAILED&error=/";
        }
    }

    //Works
    @GetMapping("createNewInvoice/userData")
    public String displayInvoiceUserPage(HttpServletRequest request,
                                         Model model) throws Exception {
        UserEntity user = this.userService.getLoggedInUser(request);
        UUID userId = user.getId();
        try {
            if (userId != null) {
                model.addAttribute("user", user);
            }
            return "userPageForInvoice";
        } catch (Exception exception) {
            return "redirect:/?message=USER_CONFIRM_FAILED&error=";
        }
    }

    //Works
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
            System.out.println("User: " + user);
            return "redirect:/createNewInvoice/customerData";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }

    //Works
    @GetMapping("createNewInvoice/customerData")
    public String displayInvoiceCustomerPage() {
        return "testAddCustomers";
    }

    //Works
    @PostMapping("createNewInvoice/customerData")
    public String addNewCustomerData(CustomerEntity customer,
                                     RedirectAttributes redirectAttributes,
                                     HttpSession session) {
        try {
            this.customerService.createCustomer(customer);
            session.setAttribute("customer", customer);
            System.out.println("Customer: " + customer);
            return "redirect:/createNewInvoice/productOrService";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
//            return "redirect:/new-invoice/";
        }
    }


    @GetMapping("createNewInvoice/productOrService")
    public String displayInvoiceProductServicePage() {
/*        model.addAttribute("addMore",
                "createNewInvoice/productOrService/add-more");*/

        return "testingAddProduct";
    }

    // Vajadzetu tikt skaidrībā ar YES NO pogām un pirms Confirm parādīt visu produktu sarakstu
    @GetMapping("createNewInvoice/productOrService/add-more")
    public String addMoreProductServicePage() {
        return "testingAddProduct";
    }

    @PostMapping("createNewInvoice/productOrService")
    public String addProductsToInvoice(
            ProductOrServiceEntity productOrService,
            RedirectAttributes redirectAttributes,
//            @RequestParam("addMore") String addMore,
            @RequestParam String action,
            HttpSession session) {
        // Saraksts, kur saglabā izvēlēto produktu ID
        List<UUID> productIDs = (List<UUID>) session.getAttribute("selectedProducts");
        // Ja šāds saraksts vēl nav izveidots (pirms AddMore), izveido to
        if (productIDs == null) {
            productIDs = new ArrayList<>();
        }
      /*  try {
            ProductOrServiceEntity createdProduct =
                    this.serviceForProducts.createProductService(productOrService);
            UUID productID = createdProduct.getId();

            productIDs.add(productID);
            session.setAttribute("selectedProducts", productIDs);
            System.out.println("Products: " + productIDs);
            if ("Yes".equals(addMore)) {
                return "redirect:/createNewInvoice/productOrService/add-more";
            } else {
                return "redirect:/createNewInvoice/signatureAndNotes";
            }
//            return "redirect:/createNewInvoice/signatureAndNotes";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }*/
        try {
            ProductOrServiceEntity createdProduct =
                    this.serviceForProducts.createProductService(productOrService);
            UUID productID = createdProduct.getId();

            productIDs.add(productID);
            session.setAttribute("selectedProducts", productIDs);
            System.out.println("Products: " + productIDs);
            if ("addMore".equals(action)) {
                return "redirect:/createNewInvoice/productOrService";
            } else if ("redirect".equals(action)) {
                return "redirect:/createNewInvoice/signatureAndNotes";
            }
            return "redirect:/createNewInvoice/signatureAndNotes";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }

    @GetMapping("createNewInvoice/signatureAndNotes")
    public String displaySignatureAndNotesPage() {
        return "signatureAndNotes";
    }

    @PostMapping("createNewInvoice/signatureAndNotes")
    public String testingConfirmSignatureAndNotesPage(@RequestParam("notes") String notes,
                                                      @RequestParam("methodOfSigning") Signature methodOfSigning,
                                                      @RequestParam("issuedAt") Date issuedAt,
                                                      @RequestParam("dueBy") Date dueBy,
                                                      RedirectAttributes redirectAttributes,
                                                      HttpSession session) {
        try {
            session.setAttribute("notes", notes);
            session.setAttribute("methodOfSigning", methodOfSigning);
            session.setAttribute("issuedAt", issuedAt);
            session.setAttribute("dueBy", dueBy);
            System.out.println("Notes: " + notes +
                    ", Signture: " + methodOfSigning +
                    "Issued On: " + issuedAt +
                    "Due by: " + dueBy);
            return "redirect:/BEFOREinvoice-overview/";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=THIS_STAGE_FAILED&error=/";
        }
    }

    // JĀATROD ŠIM LABA VIETA
    @GetMapping("BEFOREinvoice-overview/")
    public String displayConfirmPage() {
        return "invoiceConfirm";
    }

    @PostMapping("BEFOREinvoice-overview/")
    public String createInvoice(HttpSession session) throws Exception {

        List<UUID> productIds = (List<UUID>) session.getAttribute("selectedProducts");

        String invoiceNumber = (String) session.getAttribute("invoiceNumber");
        UserEntity user = (UserEntity) session.getAttribute("user");
        CustomerEntity customer = (CustomerEntity) session.getAttribute("customer");
        String notes = (String) session.getAttribute("notes");
        Signature methodOfSigning = (Signature) session.getAttribute("methodOfSigning");
        Date issuedAt = (Date) session.getAttribute("issuedAt");
        Date dueBy = (Date) session.getAttribute("dueBy");

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setNotes(notes);
        invoice.setMethodOfSigning(methodOfSigning);
        invoice.setIssuedAt(issuedAt);
        invoice.setDueBy(dueBy);
        invoice.setUser(user);
        invoice.setCustomer(customer);

        List<InvoiceProductEntity> invoiceProducts = new ArrayList<>();
        for (UUID productId : productIds) {
            ProductOrServiceEntity product = serviceForProducts.getProductById(productId);
            InvoiceProductEntity invoiceProduct = new InvoiceProductEntity();
            invoiceProduct.setInvoice(invoice);
            invoiceProduct.setProduct(product);
            invoiceProducts.add(invoiceProduct);
        }
        invoice.setInvoiceProducts(invoiceProducts);
        this.invoiceService.createNewInvoice(invoice);
        session.setAttribute("newInvoiceId", invoice.getId());

        System.out.println(invoice);
        System.out.println("Products in invoice: " + invoiceProducts);

//        session.removeAttribute("selectedProductIds");

        return "redirect:/invoice-overview/" + invoice.getId(); // Redirect to the list of invoices
    }


/*    public void testingList() {
        List<InvoiceProductEntity> allInvoiceProducts = invoiceService.getAllInvoiceProducts();
        for (InvoiceProductEntity invoicedProduct : allInvoiceProducts) {
            System.out.println("InvoiceProduct ID: " + invoicedProduct);
            ProductOrServiceEntity product = invoicedProduct.getProduct();
            if (product != null) {
                System.out.println("Product ID: " + product.getId());
            }
        }
    }*/
   /* @GetMapping("invoice-overview/{id}")
    public String displayInvoiceOverview(@PathVariable UUID id, Model model, HttpSession session){
        try {
            UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
            InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
            CurrentInvoice currentInvoice = invoiceService.getCurrentInvoiceWithProducts(invoice);
            UserEntity user = invoice.getUser();
            CustomerEntity customer = invoice.getCustomer();
            List<ProductOrServiceEntity> productDetails = new ArrayList<>();

            for (InvoiceProductEntity invoicedProduct: invoiceService.getAllInvoiceProducts()) {
                ProductOrServiceEntity product = invoicedProduct.getProduct();
                if (product !=null){
                    productDetails.add(product);
                }
            }

            model.addAttribute("invoice", invoice);
            model.addAttribute("user", user);
            model.addAttribute("customer", customer);
            model.addAttribute("products", productDetails);
            return "invoiceOverview";
        } catch (Exception exception) {
            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
                    + exception.getMessage();
        }
    }*/

    @GetMapping("invoice-overview/{id}")
    public String displayInvoiceOverview(@PathVariable UUID id, Model model, HttpSession session){
        try {
            UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
            InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
            CurrentInvoice currentInvoice = invoiceService.getCurrentInvoiceWithProducts(invoice);

            model.addAttribute("currentInvoice", currentInvoice);

            return "invoiceOverview";
        } catch (Exception exception) {
            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
                    + exception.getMessage();
        }
    }
    @PostMapping("/confirm-invoice")
    public String confirmInvoice(@ModelAttribute InvoiceEntity invoice, Model model){
        try {
//            invoiceService.createNewInvoice(invoice);

            return "redirect:/archive-invoice/"; /*+ invoice.getId();*/
        } catch (Exception exception) {
            return "redirect:/?message=CONFIRM_INVOICE_FAILED&error="
                    + exception.getMessage();
        }
    }

    @GetMapping("/archive-invoice")
    public String displayInvoicesFromArchive(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "archiveInvoices";
    }


    public double invoiceTotalPrice(ProductOrServiceEntity productOrServiceEntity,
                                    Model model){
        double price = productOrServiceEntity.getUnitPrice();
        Category taxCategory = productOrServiceEntity.getVATrate();
        double taxRate = 0.0;
        if(taxCategory==Category.REDUCED5){
            taxRate = 0.05;
        }else if(taxCategory==Category.REDUCED10){
            taxRate = 0.1;
        }else if (taxCategory==Category.REDUCED12){
            taxRate = 0.12;
        }else if(taxCategory==Category.STANDARD21){
            taxRate = 0.21;
        }
        double quantity = productOrServiceEntity.getQuantity();
        double totalPrice = price *quantity * (1 + taxRate);
        model.addAttribute("unitPrice",price);
        model.addAttribute("taxRate", taxRate);
        model.addAttribute("quantity", quantity);
        model.addAttribute("totalPrice", totalPrice);
// get price & get Tax -> price+tax = total price;-> <list> -> invoice
        return totalPrice;
    }
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