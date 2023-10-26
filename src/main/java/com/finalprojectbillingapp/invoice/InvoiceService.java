package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserEntity;
import com.finalprojectbillingapp.user.UserRepository;
import com.finalprojectbillingapp.user.UserService;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final UserRepository userRepository;
    private UserService userService;
    private HttpSession session;

//    private final SessionFactory sessionFactory;
    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceProductRepository invoiceProductRepository,
                          UserRepository userRepository,
                          UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.userRepository = userRepository;
        this.userService = userService;
//        this.sessionFactory = sessionFactory;
    }


    public InvoiceEntity createNewInvoice(InvoiceEntity invoiceEntity) throws Exception {

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

    public List<InvoiceEntity> getSortedInvoices(Sort sortParameter,
                                                 HttpServletRequest request) throws Exception {
       String loginEmail = this.userService.getLoggedInUserEmail(request);
        List<InvoiceEntity> invoices = getInvoicesByUserEmail(loginEmail);
        return switch (sortParameter) {
            case CREATED_AT -> this.invoiceRepository.sortAllByCreatedAt();
            case ISSUED_AT -> this.invoiceRepository.sortAllByIssuedAt();
            case DUE_BY -> this.invoiceRepository.sortAllByDueBy();
            case SELLER -> this.invoiceRepository.sortAllByUserName();
            case BUYER -> this.invoiceRepository.sortAllByCustomerName();
            case TOTAL -> this.invoiceRepository.sortAllByTotal();
            case CURRENCY -> this.invoiceRepository.sortAllByCurrency();
            case STATUS -> this.invoiceRepository.sortAllByStatus();
            default -> (List<InvoiceEntity>) this.invoiceRepository.findAll();
        };
    }
    @Transactional
    public List<InvoiceEntity> findAllWithUsers() {
        return invoiceRepository.findAllWithUsers();
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