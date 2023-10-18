//package com.finalprojectbillingapp.invoice;
//
//import com.finalprojectbillingapp.customer.CustomerEntity;
//import com.finalprojectbillingapp.customer.CustomerService;
//import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
//import com.finalprojectbillingapp.productOrService.ServiceForProducts;
//import com.finalprojectbillingapp.user.*;
//import jakarta.persistence.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.sql.Date;
//import java.sql.Timestamp;
//import java.util.UUID;
//
//
//
//public class InvoiceMenuController {
//    private final InvoiceRepository invoiceRepository;
//    private InvoiceService invoiceService;
//    private UserService userService;
//    private CustomerService customerService;
//    private ServiceForProducts serviceForProducts;
//
//    @ModelAttribute("invoiceData")
//    public InvoiceEntity collectInvoiceData() {
//        return new InvoiceEntity();
//    }
//    @PersistenceContext
//    private EntityManager entityManager;
//    @Autowired
//    public InvoiceMenuController (InvoiceRepository invoiceRepository,
//                             UserService userService,
//                             CustomerService customerService,
//                             ServiceForProducts serviceForProducts) {
//        this.invoiceRepository = invoiceRepository;
//        this.userService = userService;
//        this.customerService = customerService;
//        this.serviceForProducts = serviceForProducts;
//    }
//
//    // Strādā
//    @GetMapping("/new-invoice/")
//    public String testingDisplayStartInvoicePage(){
//        return "invoices";
//    }
//
//    // Strādā
//    @GetMapping("/createNewInvoice/invoiceNumber")
//    public String testingDisplayInvoiceNumberPage(Model model) {
//        model.addAttribute("invoiceData", new InvoiceEntity());
//        return "enterInvoiceNumber";
//    }
//    @PostMapping("/createNewInvoice/confirmInvoiceNumber")
//    public String testingConfirmInvoiceNumber(@ModelAttribute ("invoiceData") InvoiceEntity invoice,
//                                       HttpServletRequest request,
//                                       RedirectAttributes redirectAttributes) throws Exception {
//        try {
//            invoice.setInvoiceNumber(request.getParameter("invoiceNumber"));
//            return "redirect:/createNewInvoice/userData";
//
//        } catch (Exception exception) {
//            redirectAttributes.addFlashAttribute("error", exception.getMessage());
//            return "redirect:/?message=INVOICE_NUMBER_FAILED&error=/";
//        }
//    }
//
//    @GetMapping("createNewInvoice/userData")
//    public String testingDisplayInvoiceUserPage(HttpServletRequest request,
//                                         Model model) throws Exception {
//        UserEntity user = this.userService.getLoggedInUser(request);
//        UUID userId = user.getId();
//        try {
//            if(userId != null) {
//                model.addAttribute("user", user);
//            }
//            return "userPageForInvoice";
//        } catch (Exception exception) {
//            return "redirect:/?message=USER_CONFIRM_FAILED&error=";
//        }
//    }
//
//    @PostMapping("createNewInvoice/userData")
//    public String testingConfirmUserData(@ModelAttribute("invoiceData") InvoiceEntity invoice,
//                                  HttpServletRequest request,
//                                  RedirectAttributes redirectAttributes) throws Exception {
//
//        UserEntity loggedInUser = this.userService.getLoggedInUser(request);
//        UUID userId = loggedInUser.getId();
//
//        try {
//            UserEntity user = this.userService.editUserDetails(loggedInUser, userId);
//            invoice.setUser(user);
//            return "redirect:/createNewInvoice/customerData";
//
//        } catch (Exception exception) {
//            redirectAttributes.addFlashAttribute("error", exception.getMessage());
//            return "redirect:/new-invoice/";
//        }
//    }
//
//    @GetMapping("createNewInvoice/customerData")
//    public String testingDisplayInvoiceCustomerPage() {
//        return "testAddCustomers";
//    }
//
//    @PostMapping("createNewInvoice/customerData")
//    public String testingAddNewCustomerData(@ModelAttribute("invoiceData") InvoiceEntity invoice,
//                                     RedirectAttributes redirectAttributes) {
//        try {
//            CustomerEntity customer = new CustomerEntity();
//            this.customerService.createCustomer(customer);
//            invoice.setCustomer(customer);
//            return "redirect:/createNewInvoice/productOrService";
//        } catch (Exception exception) {
//            redirectAttributes.addFlashAttribute("error", exception.getMessage());
//            return "redirect:/?message=ADDING_CUSTOMER_FAILED&error=";
////            return "redirect:/new-invoice/";
//        }
//    }
//
//    @GetMapping("createNewInvoice/productOrService")
//    public String testingDisplayInvoiceProductServicePage(Model model){
//        model.addAttribute("addMore",
//                "createNewInvoice/productOrService/add-more");
//   /*     // Does not show the confirmation button yet
//        model.addAttribute("showConfirmationButton", false);*/
//        return "testingAddProduct";
//    }
//
//    // Vajadzetu tikt skaidrībā ar YES NO pogām un pirms Confirm parādīt visu produktu sarakstu
//    @GetMapping("createNewInvoice/productOrService/add-more")
//    public String testingAddMoreProductServicePage(){
//        return "testingAddProduct";
//    }
//
//    // NESAGLABĀ BEZ ADD MORE
//    @PostMapping("createNewInvoice/productOrService")
//    public String testingConfirmInvoiceProductServiceData(@ModelAttribute("invoiceData") InvoiceEntity invoice,
//                                                   RedirectAttributes redirectAttributes,
//                                                   @RequestParam("addMore") boolean addMore,
//                                                   HttpServletRequest request) {
//        try {
//            ProductOrServiceEntity item = new ProductOrServiceEntity();
//            this.serviceForProducts.createProductService(item);
//            invoice.setProductOrService(item);
//            if (!addMore){
//                this.serviceForProducts.createProductService(item);
//                invoice.setProductOrService(item);
//                return "redirect:/createNewInvoice/signatureAndNotes";
//            } else {
//                return "testingAddProduct";
//            }
//        } catch (Exception exception) {
//            redirectAttributes.addFlashAttribute("error", exception.getMessage());
//            return "redirect:/new-invoice/";
//        }
//    }
//
//    @GetMapping("createNewInvoice/signatureAndNotes")
//    public String testingDisplaySignatureAndNotesPage(Model model){
//        return "signatureAndNotes";
//    }
//
//    // NESAGLABĀ RĒĶINU
//    @PostMapping("createNewInvoice/signatureAndNotes")
//    public String testingConfirmSignatureAndNotesPage(@ModelAttribute("invoiceData") InvoiceEntity invoice,
//                                               RedirectAttributes redirectAttributes,
//                                               HttpServletRequest request){
//        try {
//            invoice.setNotes(request.getParameter("notes"));
//            invoice.setMethodOfSigning(Signature.valueOf(request.getParameter("signature")));
//            invoice.setIssuedAt(Date.valueOf(request.getParameter("issuedAt")));
//            invoice.setDueBy(Date.valueOf(request.getParameter("issuedAt")));
//
//            InvoiceEntity generatedInvoice = new InvoiceEntity();
//            generatedInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
//            generatedInvoice.setMethodOfSigning(invoice.getMethodOfSigning());
//            generatedInvoice.setIssuedAt(invoice.getIssuedAt());
//            generatedInvoice.setDueBy(invoice.getDueBy());
//            generatedInvoice.setUser(invoice.getUser());
//            generatedInvoice.setCustomer(invoice.getCustomer());
//            generatedInvoice.setProductOrService(invoice.getProductOrService());
//
//            this.invoiceService.createNewInvoice(generatedInvoice);
//            entityManager.flush();
//
//            return "redirect:/createNewInvoice/review-invoice";
//        } catch (Exception exception) {
//            redirectAttributes.addFlashAttribute("error", exception.getMessage());
//            return "redirect:/?message=THIS_STAGE_FAILED&error=/";
//        }
//    }
//   /* @PostMapping("createNewInvoice/generate-review")
//    public String generateInvoiceToReview(HttpSession session, Model model,
//                                          RedirectAttributes redirectAttributes) throws Exception {
//        try {
//            String invoiceNumber = (String) session.getAttribute("invoiceNumber");
//            String notes = (String) session.getAttribute("notes");
//            Signature methodOfSigning = (Signature) session.getAttribute("signature");
//            Date issuedAt = (Date) session.getAttribute("issuedAt");
//            Date dueBy = (Date) session.getAttribute("dueBy");
//            UserEntity user = (UserEntity) session.getAttribute("user");
//            CustomerEntity customer = (CustomerEntity) session.getAttribute("customer");
//            ProductOrServiceEntity productOrService =
//                    (ProductOrServiceEntity) session.getAttribute("item");
//
//            InvoiceEntity invoice = new InvoiceEntity();
//            invoice.setInvoiceNumber(invoiceNumber);
//            invoice.setNotes(notes);
//            invoice.setMethodOfSigning(methodOfSigning);
//            invoice.setIssuedAt(issuedAt);
//            invoice.setDueBy(dueBy);
//            invoice.setUser(user);
//            invoice.setCustomer(customer);
//            invoice.setProductOrService(productOrService);
//
//            this.invoiceService.createNewInvoice(invoice);
//            entityManager.flush();
//            return "redirect:/createNewInvoice/review-invoice";
//
//        } catch (Exception exception) {
//            redirectAttributes.addFlashAttribute("error", exception.getMessage());
//            return "redirect:/?message=GENERATE_FOR_REVIEW_FAILED&error=/";
//        }
//    }*/
//
//    // REMOVE SESSION ATTRIBUTES IN FINAL CONFIRM
//    // TESTING TESTING TESTING
//    @GetMapping("createNewInvoice/review-invoice")
//    public String testingDisplayInvoiceOverview(){
//        return "testingInvoiceConfirm";
//    }
//
////    @GetMapping("/invoice-overview/{id}")
////    public String displayInvoiceOverview(@PathVariable UUID id, Model model){
////        try {
////            InvoiceEntity invoice = invoiceService.getInvoiceById(id);
////            model.addAttribute("invoice", invoice);
////            UserEntity user = invoice.getUser();
////            CustomerEntity customer = invoice.getCustomer();
////            ProductOrServiceEntity productsOrServices = invoice.getProductOrService();
////
////            model.addAttribute("user", user);
////            model.addAttribute("customer", customer);
////            model.addAttribute("productsOrServices", productsOrServices);
////
////            // TESTING TESTING TESTING
////            return "testingInvoiceConfirm";
////        } catch (Exception exception) {
////            return "redirect:/?message=INVOICE_OVERVIEW_FAILED&error="
////                    + exception.getMessage();
////        }
////    }
//
//    //
////    @PostMapping("createNewInvoice/signatureAndNotes")
////
////    @GetMapping("/archive-invoice")*/
//    public String displayInvoicesFromArchive(Model model){
//        model.addAttribute("invoices",invoiceService.getAllInvoices());
//        return "archiveInvoices";
//    }
//
//
//
//
///*    @GetMapping("/new-invoice/")
//    public String displayInvoicePage(HttpServletRequest request,
//                                     Model model)throws Exception{
//        // UserService userService = new UserService();
//
//        UserEntity userEntity = this.userService.getLoggedInUser(request);
//        this.userService.editUserDetails(userEntity, userEntity.getId());
//        model.addAttribute("user", userEntity);
//        model.addAttribute("name", userEntity.getName());
//        model.addAttribute("taxpayerNo", userEntity.getTaxpayerNo());
//        model.addAttribute("legalAddress", userEntity.getLegalAddress());
//        model.addAttribute("country", userEntity.getCountry().getDisplayName());
//        model.addAttribute("BankName", userEntity.getBankName());
//        model.addAttribute("accountNo", userEntity.getAccountNo());
//        model.addAttribute("email", userEntity.getEmail());
//
//        return "create_invoice";
//    }*/
//
//
//    //  @PostMapping("/archive-invoice")
//
//
//}