package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserEntity;
import com.finalprojectbillingapp.user.UserRepository;
import com.finalprojectbillingapp.user.UserService;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

//    public static String getUserIdFromCookies (HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        String userId = null;
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("loggedInUserId")) {
//                    userId = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        return userId;
//    }



    // Method to remove all session attributes
//    public void removeAllSessionAttributes() {
//        Enumeration<String> attributeNames = session.getAttributeNames();
//        while (attributeNames.hasMoreElements()) {
//            String attributeName = attributeNames.nextElement();
//            session.removeAttribute(attributeName);
//        }
//    }

    public List<InvoiceEntity> getInvoicesByUserEmail(String userEmail) {
        return invoiceRepository.findInvoicesByUserLoginEmail(userEmail);
    }

//    public List<InvoiceEntity> getInvoicesForLoggedInUser(HttpServletRequest request) throws Exception {
//        InvoiceEntity invoice = new InvoiceEntity();
//        List<InvoiceEntity> archivedInvoices = new ArrayList<>();
//        try {
//            UserEntity user = this.userService.getLoggedInUser(request);
//            String email = this.userService.getLoggedInUserEmail(request);
//
//            sessionFactory.openSession();
//
//            String queryEmail = "FROM users WHERE loginEmail LIKE %?%";
//            Query<UserEntity> query = sessionFactory.createEntityManager()
//
//        } catch (Exception exception){
//            throw new Exception(exception.getMessage());
//        }
//    }
}