package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ProductServiceRepository;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
;
import jakarta.persistence.Cacheable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;

import java.sql.Date;
import java.util.*;

import static com.finalprojectbillingapp.invoice.Status.*;
import static java.lang.String.*;


@Controller

public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final ProductServiceRepository productServiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private InvoiceService invoiceService;
    private UserService userService;
    private CustomerService customerService;
    private ServiceForProducts serviceForProducts;
    private UserRepository userRepository;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository,
                             UserService userService,
                             CustomerService customerService,
                             ServiceForProducts serviceForProducts,
                             ProductServiceRepository productServiceRepository,
                             InvoiceService invoiceService,
                             InvoiceProductRepository invoiceProductRepository,
                             UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.serviceForProducts = serviceForProducts;
        this.productServiceRepository = productServiceRepository;
        this.invoiceService = invoiceService;
        this.invoiceProductRepository = invoiceProductRepository;
        this.userRepository = userRepository;
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
//        session.setAttribute("htmlContent", htmlContent);
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


    @GetMapping("createNewInvoice/userData")
    public String displayInvoiceUserPage(HttpServletRequest request, Model model) throws Exception {
        UserEntity user = this.userService.getLoggedInUser(request);
        model.addAttribute("user", user);
        return "userPageForInvoice";
    }

    @PostMapping("createNewInvoice/userData")
    public String handleEditUserDataForm(@ModelAttribute("user") UserEntity editedUser,
                                         HttpServletRequest request,
                                         HttpSession session) throws Exception {
        UserEntity loggedInUser = userService.getLoggedInUser(request);
        UUID userId = loggedInUser.getId();

        try {
            loggedInUser.setName(editedUser.getName());
            loggedInUser.setTaxpayerNo(editedUser.getTaxpayerNo());
            loggedInUser.setTaxpayerType(editedUser.getTaxpayerType());
            loggedInUser.setLegalAddress(editedUser.getLegalAddress());
            loggedInUser.setCountry(editedUser.getCountry());
            loggedInUser.setBankName(editedUser.getBankName());
            loggedInUser.setAccountNo(editedUser.getAccountNo());

            this.userService.editUserDetails(loggedInUser, userId);
            session.setAttribute("userId", userId);
            System.out.println(loggedInUser);

            return "redirect:/createNewInvoice/customerData";
        } catch (Exception exception) {
            // Handle errors if needed
            return "redirect:/createNewInvoice/userData";
        }
    }

    @GetMapping("/createNewInvoice/customerData")
    public String displayAddCustomerPage(Model model) throws Exception {
        CustomerEntity selectedCustomer = (CustomerEntity)
                model.getAttribute("selectedCustomer");
        System.out.println(selectedCustomer);
        if (selectedCustomer != null) {
//            selectedCustomer = this.customerService.getCustomerById(selectedCustomer.getId());
            model.addAttribute("selectedCustomer1", selectedCustomer);
        } else {
            CustomerEntity customerEntity = new CustomerEntity();
            model.addAttribute("newCustomer", customerEntity);
        }
        return "testAddCustomers";
    }

    //Works
    @PostMapping("createNewInvoice/customerData")
    public String addNewCustomerData(@ModelAttribute("newCustomer") CustomerEntity newCustomer,
                                     RedirectAttributes redirectAttributes,
                                     HttpSession session,
                                     Model model) {
        try {
            UUID selectedCustomerId = (UUID) model.getAttribute("selectedCustomerId");

            if (selectedCustomerId != null) {
                CustomerEntity selectedCustomer = this.customerService.getCustomerById(selectedCustomerId);
                this.customerService.editCustomerDetails(selectedCustomerId, selectedCustomer);
                session.setAttribute("customerId", selectedCustomer.getId());
                System.out.println("edited customer: " + selectedCustomer);
            } else {
                this.customerService.createCustomer(newCustomer);
                session.setAttribute("customerId", newCustomer.getId());
                System.out.println("New customer: " + newCustomer);
            }
            return "redirect:/select-currency";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
        }
    }

    @GetMapping("select-currency")
    public String displayGetCurrency() {
        return "getCurrency";
    }

    @PostMapping("select-currency")
    public String getCurrency(@RequestParam("currency") Currency selectedCurrency,
                              HttpSession session) {
        session.setAttribute("currency", selectedCurrency);
        return "redirect:/createNewInvoice/productOrService";
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


    @PostMapping("createNewInvoice/productOrService")
    public String addProductsToInvoice(
            ProductOrServiceEntity productOrService,
            RedirectAttributes redirectAttributes,
            @RequestParam String action,
            HttpSession session) {

        List<UUID> productIDs = (List<UUID>) session.getAttribute("selectedProducts");

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
        Currency currency = (Currency) session.getAttribute("currency");

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setNotes(notes);
        invoice.setMethodOfSigning(methodOfSigning);
        invoice.setIssuedAt(issuedAt);
        invoice.setDueBy(dueBy);
        invoice.setUser(this.userService.getUserById(userId));
        invoice.setCustomer(this.customerService.getCustomerById(customerId));
        invoice.setCurrency(currency);


        List<ProductOrServiceEntity> productObjects = new ArrayList<>();
        List<InvoiceProductEntity> invoiceProducts = new ArrayList<>();
        double totalPrice = 0;
        for (UUID productId : productIds) {
            ProductOrServiceEntity product = serviceForProducts.getProductById(productId);
            InvoiceProductEntity invoiceProduct = new InvoiceProductEntity();
            invoiceProduct.setInvoice(invoice);
            invoiceProduct.setProduct(product);
            invoiceProducts.add(invoiceProduct);
            productObjects.add(product);
            totalPrice += product.getTotalPerProduct();
        }

        session.setAttribute("invoicedProduct", productObjects);
        invoice.setInvoiceProducts(invoiceProducts);

        invoice.setTotalPrice(totalPrice);

        this.invoiceService.createNewInvoice(invoice);
        session.setAttribute("newInvoiceId", invoice.getId());
        session.setAttribute("invoice", this.invoiceService.getInvoiceById(
                (UUID) session.getAttribute("newInvoiceId")));
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

    @PostMapping("/confirm-invoice")
    public String confirmInvoice(@ModelAttribute InvoiceEntity invoice,
                                 Model model,
                                 HttpSession session) {
        try {
            return "redirect:/archive-invoice";
        } catch (Exception exception) {
            return "redirect:/?message=CONFIRM_INVOICE_FAILED&error="
                    + exception.getMessage();
        }
    }

    @GetMapping("/create-pdf")
    public String printToPdf(Model model, HttpSession session) {
        try {
            UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
            InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
            List<ProductOrServiceEntity> products =
                    this.invoiceService.getProductObjectsForInvoice(invoiceId);
            model.addAttribute("invoice", invoice);
            model.addAttribute("products", products);

            return "invoiceOverviewPrint";
        } catch (Exception exception) {
            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
                    + exception.getMessage();
        }
    }

    @GetMapping("/displaySessionAttributes/")
    public String displaySessionAttributes(
            HttpSession session,
            HttpServletRequest request,
            Model model) throws Exception {


        String email = this.userService.getLoggedInUserEmail(request);
        List<CustomerEntity> customerList = new ArrayList<>(
                this.customerService.getAllCustomerByUserLoginEmail(email));
        System.out.println(customerList);

        return "invoiceOverviewPrint";
    }

    @PostMapping("/cancel-invoice")
    public String handleCancelInvoice(HttpSession session) throws Exception {
        UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
        InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
        invoice.setStatus(VOID);
        this.invoiceRepository.save(invoice);
        return "mainPageForUser";
    }

    @GetMapping("/archive-invoice")
    public String displayInvoicesFromArchive(Model model, HttpServletRequest request) throws Exception {
        try {
            String loginEmail = this.userService.getLoggedInUserEmail(request);
            if (loginEmail != null) {
                List<InvoiceEntity> invoices = this.invoiceService.getInvoicesByUserEmail(loginEmail);
                model.addAttribute("invoices", invoices);
                request.getSession().setAttribute("invoices", invoices);
                System.out.println(invoices);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            model.addAttribute("errorMessage",
                    "An error occurred while fetching data");
        }
        return "sortInvoices";
    }


//    @GetMapping("/sort-invoices")
//    public String sortInvoices(@RequestParam("sortParameter") Sort sortParameter, Model model,
//                               HttpServletRequest request) {
//        try {
//            String loginEmail = this.userService.getLoggedInUserEmail(request);
//            if (loginEmail != null) {
//                List<InvoiceEntity> invoices =
//                        this.invoiceService.getSortedInvoices(sortParameter, request, loginEmail);
//                model.addAttribute("invoices", invoices);
//            }
//        } catch (Exception exception) {
//
//            exception.printStackTrace();
//            model.addAttribute("errorMessage", "An error occurred while fetching data");
//        }
//        return "sortInvoices";
//    }
//@GetMapping("/sort-invoices")
//public String sortInvoices(@RequestParam("sortParameter") Sort sortParameter,
//                           Model model,
//                           HttpServletRequest request) throws Exception {
//  try {
//        String email = this.userService.getLoggedInUserEmail(request);
//        model.addAttribute("sortParameter", sortParameter);
//        List<InvoiceEntity> sortedInvoices = invoiceService.getSortedInvoices(sortParameter, request);
//
//        model.addAttribute("invoices", sortedInvoices);
//        } catch (Exception exception) {
//                exception.printStackTrace();
//                model.addAttribute("errorMessage", "An error occurred while fetching data");
//                }
//                return "sortInvoices";
//                }

    @GetMapping("/sort-invoices")
    public String sortInvoices(@RequestParam("sortParameter") Sort sortParameter, Model model,
                               HttpServletRequest request) {
        try {
            String loginEmail = this.userService.getLoggedInUserEmail(request);
            request.getSession().setAttribute("sortParameter", sortParameter);
            if (loginEmail != null) {
                List<InvoiceEntity> invoices =
                        (List<InvoiceEntity>) request.getSession().getAttribute("invoices");
                if (invoices != null) {
                    switch (sortParameter) {
                        case CREATED_AT:
                            invoices.sort(Comparator.comparing(InvoiceEntity::getCreatedAt));
                            break;
                        case ISSUED_AT:
                            invoices.sort(Comparator.comparing(InvoiceEntity::getIssuedAt));
                            break;
                        case SELLER:
                            invoices = this.invoiceService.sortAllByUser(invoices);
                            break;
                        case DUE_BY:
                            invoices.sort(Comparator.comparing(InvoiceEntity::getDueBy));
                            break;
                        case BUYER:
                            invoices = this.invoiceService.sortAllByCustomer(invoices);
                            break;
                        case TOTAL:
                            invoices.sort(Comparator.comparing(InvoiceEntity::getTotalPrice));
                            break;
                        case CURRENCY:
                            invoices.sort(Comparator.comparing(invoice -> invoice.getCurrency().name()));
                            break;
                        case STATUS:
                            invoices.sort(Comparator.comparing(invoice -> invoice.getStatus().name()));
                            break;
                        default:
                            this.invoiceRepository.findAll();
                    }
                    model.addAttribute("invoices", invoices);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while fetching data");
        }
        return "sortInvoices";
    }
}




