package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserRepository;
import com.finalprojectbillingapp.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final UserRepository userRepository;
    private UserService userService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceProductRepository invoiceProductRepository,
                          UserRepository userRepository,
                          UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    public InvoiceEntity createNewInvoice(InvoiceEntity invoiceEntity) {

        this.invoiceRepository.save(invoiceEntity);
        return invoiceEntity;
    }

    public List<InvoiceEntity> getAllInvoices() {
        return (ArrayList<InvoiceEntity>)
                this.invoiceRepository.findAll();
    }

    public InvoiceEntity getInvoiceById(UUID id) throws Exception {
        return this.invoiceRepository.findById(id).orElseThrow();
    }

    // NEW
    public List<InvoiceProductEntity> getAllInvoiceProducts() {
        return invoiceProductRepository.findAll();
    }

    public List<InvoiceProductEntity> getProductsForInvoice(UUID invoiceId) {
        return invoiceProductRepository.findAllByInvoice_id(invoiceId);
    }

    public List<ProductOrServiceEntity> getProductObjectsForInvoice(UUID invoiceId) {
        List<InvoiceProductEntity> invoiceProducts = this.invoiceProductRepository.findAllByInvoice_id(invoiceId);
        List<ProductOrServiceEntity> products = new ArrayList<>();

        for (InvoiceProductEntity invoiceProduct : invoiceProducts) {
            products.add(invoiceProduct.getProduct());
        }
        return products;
    }


    public List<InvoiceEntity> getInvoicesByUserEmail(String userEmail) {
        return invoiceRepository.findInvoicesByUserLoginEmail(userEmail);
    }


    public List<InvoiceEntity> sortAllByUser(List<InvoiceEntity> invoices) {
        invoices.sort(Comparator.comparing(invoice -> invoice.getUser().getName()));
        return invoices;
    }

    public List<InvoiceEntity> sortAllByCustomer(List<InvoiceEntity> invoices) {
        invoices.sort(Comparator.comparing(invoice -> invoice.getCustomer().getName()));
        return invoices;
    }
}