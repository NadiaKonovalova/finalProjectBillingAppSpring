package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerService;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.productOrService.ProductServiceRepository;
import com.finalprojectbillingapp.productOrService.ServiceForProducts;
import com.finalprojectbillingapp.user.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.WebContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

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
                                         Model model, HttpSession session) throws Exception {
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

    //    Works
    @PostMapping("createNewInvoice/userData")
    public String confirmUserData(@ModelAttribute("user") UserEntity user,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) throws Exception {
        UserEntity loggedInUser = this.userService.getLoggedInUser(request);
        UUID userId = loggedInUser.getId();

        try {
            UserEntity user1 = this.userService.editUserDetailsForInvoice(loggedInUser, userId);
            session.setAttribute("userId", user1.getId());
            this.userService.createUser(user1);
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
   // @PostMapping("createNewInvoice/productOrService")
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
            return "redirect:/archive-invoice";
        } catch (Exception exception) {
            return "redirect:/?message=CONFIRM_INVOICE_FAILED&error="
                    + exception.getMessage();
        }
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

    @PostMapping("/cancel-invoice")
    public String handleCancelInvoice(HttpSession session) throws Exception {
        UUID invoiceId = (UUID) session.getAttribute("newInvoiceId");
        InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
        invoice.setStatus(Status.VOID);
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
                System.out.println(invoices);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            model.addAttribute("errorMessage",
                    "An error occurred while fetching data");
        }
        return "archiveInvoices";
    }
    /*  @GetMapping("/generate-invoice/")
    public String generateInvoice(Model model) {
        model.addAttribute("to", "BillingApp");
        System.out.println("pdf is created");
       return "invoiceOverview";
    }*/
   /* @GetMapping("/generate-pdf-invoice")
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
       } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/generate-pdf-invoice")
    public void generatePdfInvoice(HttpServletResponse response, CreatePdfFile createPdfFile, HTML html) {
    }
    try {
      String htmlContent = createPdfFile.parseThymeleafTemplate(); // Здесь используйте метод для генерации HTML из Thymeleaf

       response.setContentType("application/pdf");
       response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
       OutputStream outputStream = response.getOutputStream();
       createPdfFile.generatePdfFromHtml(html);
   } catch (IOException e) {
        e.printStackTrace();
    }
    } catch (com.lowagie.text.DocumentException e) {
        throw new RuntimeException(e);
    }*/
    /*@RequestMapping(path = "/")
   public String getOrderPage(Model model) throws IOException {
       InvoiceEntity invoice = ();
       model.addAttribute("invoiceOverview", invoice);
       return "invoiceOverview";
   }

    @RequestMapping(path = "/pdf")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {


       InvoiceEntity invoice = InvoiceHelper.getOrder();



        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("invoiceOverview", invoice);
        String invoiceHtml = templateEngine.process("invoiceOverview", context);



        ByteArrayOutputStream target = new ByteArrayOutputStream();


        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");


        HtmlConverter.convertToPdf(invoiceHtml, target, converterProperties);


        byte[] bytes = target.toByteArray();




        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }*/

   /* @RestController
    public class PdfController {

        @GetMapping("/convertToPdf")
        public ResponseEntity<byte[]> convertToPdf() {
            try {
                String htmlContent = "";
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();


                ConverterProperties converterProperties = new ConverterProperties();
                converterProperties.setBaseUri("file:/path/to/your/html/");

                HtmlConverter.convertToPdf(htmlContent, pdfOutputStream, converterProperties);

                byte[] pdfBytes = pdfOutputStream.toByteArray();

                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdfBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity
                        .badRequest()
                        .body(new byte[0]);
            }
        }
    }*/
  /*  @GetMapping("/convertToPdf")
    public ResponseEntity<byte[]> convertToPdf() {
        try {
            String htmlContent ="<table id="invoicepdf"> </table>";


            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();


            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setBaseUri("classpath:/templates/invoiceOverview/"); // Путь к вашим ресурсам (стили, изображения)

            HtmlConverter.convertToPdf(htmlContent, pdfOutputStream, converterProperties);

            byte[] pdfBytes = pdfOutputStream.toByteArray();

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(new byte[0]);
        }
    }*/

    @GetMapping("/convertToPdf")
    public ResponseEntity<byte[]> convertToPdf() {
        try {
            String htmlContent = "<html xmlns:th=\"http://www.thymeleaf.org\">" +
                    " <table style=\"width: 100%; border-collapse: collapse;\">\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "                <div class=\"invoice-info\">\n" +
                    "                    <p>Invoice No.: &nbsp;<span th:text=\"${invoice.invoiceNumber}\"></span></p>\n" +
                    "                    <p>Issued on: &nbsp;&nbsp;&nbsp;<span th:text=\"${invoice.issuedAt}\"></span></p>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "                <table style=\"width: 100%;\">\n" +
                    "                    <tr>\n" +
                    "                        <td style=\"width: 50%;\">\n" +
                    "                            <div class=\"user-info\">\n" +
                    "                                <p class=\"section-title\" style=\"font-weight: bold;\">Seller:</p>\n" +
                    "                                <p>Name: <span th:text=\"${invoice.user.name}\"></span></p>\n" +
                    "                                <p>Taxpayer number: <span th:text=\"${invoice.user.taxpayerNo}\"></span></p>\n" +
                    "                                <p>Address: <span th:text=\"${invoice.user.legalAddress}\"></span></p>\n" +
                    "                                <p>Country: <span th:text=\"${invoice.user.country.getDisplayCountryName()}\"></span></p>\n" +
                    "                            </div>\n" +
                    "                        </td>\n" +
                    "                        <td style=\"width: 50%;\">\n" +
                    "                            <div class=\"customer-info\">\n" +
                    "                                <p class=\"section-title\" style=\"font-weight: bold;\">Buyer:</p>\n" +
                    "                                <p>Customer name: <span th:text=\"${invoice.customer.name}\"></span></p>\n" +
                    "                                <p>Taxpayer number: <span th:text=\"${invoice.customer.taxpayerNo}\"></span></p>\n" +
                    "                                <p>Address: <span th:text=\"${invoice.customer.legalAddress}\"></span></p>\n" +
                    "                                <p>Country: <span th:text=\"${invoice.customer.country.getDisplayCountryName()}\"></span></p>\n" +
                    "                            </div>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "            <div class=\"product-service-info\">\n" +
                    "                <p class=\"section-title\" style=\"font-weight: bold;\">Product/Service:</p>\n" +
                    "                <table style=\"width: 100%; border-collapse: collapse;\">\n" +
                    "                    <tr>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Name</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Quantity</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Unit</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Unit price</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Currency</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Total per item</th>\n" +
                    "                    </tr>\n" +
                    "                    <tr th:each=\"product : ${products}\">\n" +
                    "                        <td th:text=\"${product.name}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${#numbers.formatDecimal(product.quantity,1,2,'COMMA')}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${product.unit}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${#numbers.formatDecimal(product.unitPrice,1, 2,'COMMA')}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${product.currency}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${#numbers.formatDecimal(product.totalPerProduct,1, 2,'COMMA')}\" style=\"border: 1px solid #ccc;\">\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td></td>\n" +
                    "                        <td></td>\n" +
                    "                        <td></td>\n" +
                    "                        <td></td>\n" +
                    "                        <td style=\"font-weight: bold;\">Total:</td>\n" +
                    "\n" +
                    "                        <td style=\"border: 1px solid #ccc; text-align: left;\"\n" +
                    "                            th:text=\"${#numbers.formatDecimal(invoice.totalPrice,1,2,'COMMA')}\">\n" +
                    "                        </td>\n" +
                    "<!--                        <td style=\"border: 1px solid #ccc; text-align: left;\"-->\n" +
                    "<!--                            th:text=\"${#numbers.formatDecimal(invoice.totalPrice,1,2,'COMMA') + ' ' + product.currency}\"-->\n" +
                    "<!--                    </td>-->\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "<!--        <tr>-->\n" +
                    "<!--            <td style=\"padding: 10px; border: 1px solid #ccc; text-align: right;\">-->\n" +
                    "<!--                <div class=\"totalPrice\">-->\n" +
                    "<!--                    <p>Total: <span th:text=\"${invoice.totalPrice}\"  style=\"text-align: right;\"></span></p>-->\n" +
                    "<!--                </div>-->\n" +
                    "<!--            </td>-->\n" +
                    "<!--        </tr>-->\n" +
                    "\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "                <div class=\"invoice-data\">\n" +
                    "                    <p>Notes: <span th:text=\"${invoice.notes}\"></span></p>\n" +
                    "                    <p>Issued on: <span th:text=\"${invoice.issuedAt}\"></span></p>\n" +
                    "                    <p>Due by: <span th:text=\"${invoice.dueBy}\"></span></p>\n" +
                    "                    <p>Signature: <span th:text=\"${invoice.methodOfSigning.getDisplaySignatureName()}\"></span></p>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>";

            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();


            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(pdfOutputStream);


            byte[] pdfBytes = pdfOutputStream.toByteArray();


            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(new byte[0]);
        }
    }

}






