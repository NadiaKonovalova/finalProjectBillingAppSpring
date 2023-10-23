package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ProductServiceRepository;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import javax.swing.text.html.HTML;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final ProductServiceRepository productServiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
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
                             InvoiceService invoiceService,
                             InvoiceProductRepository invoiceProductRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.serviceForProducts = serviceForProducts;
        this.productServiceRepository = productServiceRepository;
        this.invoiceService = invoiceService;
        this.invoiceProductRepository = invoiceProductRepository;
    }

    // Works
    @GetMapping("/new-invoice/")
    public String displayStartInvoicePage(HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            session.removeAttribute(attributeName);
        }
        return "invoices";
    }

    // Works
    @GetMapping("/createNewInvoice/invoiceNumber")
    public String displayInvoiceNumberPage(HttpSession session) {
        InvoiceEntity invoice = new InvoiceEntity();
        UUID invoiceId = invoice.getId();
        session.setAttribute("invoiceData", invoice);
        session.setAttribute("invoiceId", invoiceId);
        return "enterInvoiceNumber";
    }
    @PostMapping("/createNewInvoice/invoiceNumber")
    public String confirmInvoiceNumber(@RequestParam("invoiceNumber") String invoiceNumber,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) throws Exception {
        try {
            session.setAttribute("invoiceNumber", invoiceNumber);
            System.out.println("Invoice number: " + invoiceNumber);

            return "redirect:/createNewInvoice/userData/";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=INVOICE_NUMBER_FAILED&error=/";
        }
    }
  // @PostMapping("/createNewInvoice/confirmInvoiceNumber")


    //Works
    @GetMapping("createNewInvoice/userData/")
    public String displayInvoiceUserPage(HttpServletRequest request,
                                         Model model, HttpSession session) throws Exception {
        UserEntity user = this.userService.getLoggedInUser(request);
        UUID idd = user.getId();
        try {
            if (id != null) {
                model.addAttribute("user", user);
            }
            return "userPageForInvoice";
        } catch (Exception exception) {
            return "redirect:/?message=USER_CONFIRM_FAILED&error=";
        }
    }

    @PostMapping("createNewInvoice/userDate")
    //Works
//    @PostMapping("createNewInvoice/userData")
    public String confirmUserData(@ModelAttribute("user") UserEntity user,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) throws Exception {
        UserEntity loggedInUser = this.userService.getLoggedInUser(request);
        UUID userId = loggedInUser.getId();

        try {
            UserEntity user1 = this.userService.editUserDetailsForInvoice(loggedInUser, userId);
            this.userService.createUser(user1);
            session.setAttribute("user", user1);
            System.out.println("User: " + user1);

            return "redirect:/createNewInvoice/customerData";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/new-invoice/";
        }
    }

    //Works
    @GetMapping("createNewInvoice/customerData")
    public String displayInvoiceCustomerPage(HttpSession session) {
        return "testAddCustomers";
    }

    //Works
    @PostMapping("createNewInvoice/customerData")
    public String addNewCustomerData(CustomerEntity customer,
                                     RedirectAttributes redirectAttributes,
                                     HttpSession session) {
        try {
            this.customerService.createCustomer(customer);
            session.setAttribute("customerId", customer.getId());
            this.customerService.getCustomerById(customer.getId());
            System.out.println("Customer: " + customer);
            return "redirect:/createNewInvoice/productOrService";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
//            return "redirect:/new-invoice/";
        }
    }

    @GetMapping("createNewInvoice/productOrService")
    public String displayInvoiceProductServicePage(HttpSession session) throws Exception {
        UUID userID = (UUID) session.getAttribute("userId");
        if (userID != null) {
            UserEntity user1 = this.userService.getUserById(userID);
            if (user1.getTaxpayerType().equals(Type.VAT_PAYER)) {
                return "testingAddProduct";
            } else {
                return "testingAddProductNoVAT";
            }
        } else {
            session.setAttribute("error", "User ID is missing.");
            return "mainPageForUser";
        }
    }

    @GetMapping("createNewInvoice/productOrService/add-more")
    public String addMoreProductServicePage() {

        return "testingAddProduct";
    }

    @PostMapping("createNewInvoice/productOrService")
    public String addProductsToInvoice(
            ProductOrServiceEntity productOrService,
            RedirectAttributes redirectAttributes,
            @RequestParam String action,
            HttpSession session) {

        // Saraksts, kur saglabā izvēlēto produktu ID
        List<UUID> productIDs = (List<UUID>) session.getAttribute("selectedProducts");
        // Ja šāds saraksts vēl nav izveidots (pirms AddMore), izveido to
        if (productIDs == null) {
            productIDs = new ArrayList<>();
        }
        try {
            ProductOrServiceEntity createdProduct =
                    this.serviceForProducts.createProductService(productOrService);
            UUID userID = (UUID) session.getAttribute("userId");
            UserEntity user1 = this.userService.getUserById(userID);
            if (user1.getTaxpayerType().equals(Type.VAT_PAYER)) {
                double totalPrice = this.serviceForProducts.calculatePricePerProductWithVAT(createdProduct);
                createdProduct.setTotalPerProduct(totalPrice);
                System.out.println(totalPrice);
            } else {
                double totalPrice = this.serviceForProducts.calculatePricePerProductNoVAT(createdProduct);
                createdProduct.setTotalPerProduct(totalPrice);
                System.out.println(totalPrice);
            }
            productServiceRepository.save(createdProduct);
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
        String invoiceNumber = (String) session.getAttribute("invoiceNumber");

        UUID userId = (UUID) session.getAttribute("userId");
        UUID customerId = (UUID) session.getAttribute("customerId");

        String notes = (String) session.getAttribute("notes");
        Signature methodOfSigning = (Signature) session.getAttribute("methodOfSigning");
        Date issuedAt = (Date) session.getAttribute("issuedAt");
        Date dueBy = (Date) session.getAttribute("dueBy");

        UUID invoiceId = (UUID) session.getAttribute("invoiceId");
        List<UUID> productIds = (List<UUID>) session.getAttribute("selectedProducts");

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setNotes(notes);
        invoice.setMethodOfSigning(methodOfSigning);
        invoice.setIssuedAt(issuedAt);
        invoice.setDueBy(dueBy);
        invoice.setUser(this.userService.getUserById(userId));
        invoice.setCustomer(this.customerService.getCustomerById(customerId));

        List<InvoiceProductEntity> invoiceProducts = new ArrayList<>();
        double totalPrice = 0;
        for (UUID productId : productIds) {
            ProductOrServiceEntity product = serviceForProducts.getProductById(productId);
            InvoiceProductEntity invoiceProduct = new InvoiceProductEntity();
            invoiceProduct.setInvoice(invoice);
            invoiceProduct.setProduct(product);
            invoiceProducts.add(invoiceProduct);
            totalPrice += product.getTotalPerProduct();
        }

        invoice.setInvoiceProducts(invoiceProducts);
        invoice.setTotalPrice(totalPrice);

        this.invoiceService.createNewInvoice(invoice);
        session.setAttribute("newInvoiceId", invoice.getId());
        System.out.println(invoice);
        System.out.println("Products in invoice: " + invoiceProducts);
        return "redirect:/invoice-overview/" + invoice.getId();
    }

    @GetMapping("invoice-overview/{id}")
    public String displayInvoiceOverview(@PathVariable UUID id, Model model, HttpSession session) {
        try {
            UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
            InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
            List<ProductOrServiceEntity> products = this.invoiceService.getProductObjectsForInvoice(invoiceId);
            model.addAttribute("invoice", invoice);
            model.addAttribute("products", products);

            return "invoiceOverview";
        } catch (Exception exception) {
            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
                    + exception.getMessage();
        }
    }

    // Bezjēdzīgs starpkods, kas tikai pārvirza uz archive-invoice
    @PostMapping("/confirm-invoice")
    public String confirmInvoice(@ModelAttribute InvoiceEntity invoice,
                                 Model model,
                                 HttpSession session) {
        try {
            return "redirect:/generate-pdf-invoice";
        } catch (Exception exception) {
            return "redirect:/?message=CONFIRM_INVOICE_FAILED&error="
                    + exception.getMessage();
        }
    }

    @PostMapping("/cancel-invoice")
    public String handleCancelInvoice(HttpSession session) throws Exception {
        UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
        InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
        invoice.setStatus(Status.VOID);
        this.invoiceRepository.save(invoice);
        return "mainPageForUser";
    }

    /*   @GetMapping("/generate-invoice/")
       public String generateInvoice(Model model) {
           model.addAttribute("to", "BillingApp");
           System.out.println("pdf is created");
           return "invoiceOverview";
       } */
    @GetMapping("/generate-pdf-invoice")
    public void generatePdfInvoice(HttpServletResponse response, CreatePdfFile createPdfFile, HTML html) {

        try {
            String htmlContent = createPdfFile.parseThymeleafTemplate();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
            OutputStream outputStream = response.getOutputStream();
            createPdfFile.generatePdfFromHtml(html);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.lowagie.text.DocumentException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/archive-invoice")
    public String displayInvoicesFromArchive(Model model, HttpServletRequest request) throws Exception {
        String loginEmail = this.userService.getLoggedInUserEmail(request);
        if (loginEmail != null) {
            List<InvoiceEntity> invoices = this.invoiceService.getInvoicesByUserEmail(loginEmail);
            model.addAttribute("invoices", invoices);
            System.out.println(invoices);
        }
        return "archiveInvoices";
    }

    @GetMapping("/displaySessionAttributes")
    public String displaySessionAttributes(HttpSession session, HttpServletRequest request) throws Exception {

//        Enumeration<String> attributeNames = session.getAttributeNames();
//
//        while (attributeNames.hasMoreElements()) {
//            String attributeName = attributeNames.nextElement();
//            Object attributeValue = session.getAttribute(attributeName);
//
//            System.out.println("Attribute Name: " + attributeName);
//            System.out.println("Attribute Value: " + attributeValue);
//        }
        String email = this.userService.getLoggedInUserEmail(request);
        System.out.println(email);
        // You can return a view name or perform any other necessary operations
        return "sessionAttributesView";
    }



}




