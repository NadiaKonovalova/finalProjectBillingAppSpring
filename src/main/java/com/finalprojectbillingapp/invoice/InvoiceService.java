package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import com.finalprojectbillingapp.user.UserRepository;
import com.finalprojectbillingapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final UserRepository userRepository;
    private UserService userService;
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