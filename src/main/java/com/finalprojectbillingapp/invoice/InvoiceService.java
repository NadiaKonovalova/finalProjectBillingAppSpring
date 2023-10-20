package com.finalprojectbillingapp.invoice;

import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceProductRepository invoiceProductRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
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


    public Map<InvoiceEntity, List<ProductOrServiceEntity>> getInvoiceAndProductMap(List<InvoiceProductEntity> invoiceProducts) {
        invoiceProducts = invoiceProductRepository.findAll();
        // Each invoice from the InvoiceProductEntity is associated with specific ID
        Map<InvoiceEntity, List<ProductOrServiceEntity>> invoiceProductMap = new HashMap<>();

        for (InvoiceProductEntity invoiceProduct : invoiceProducts) {
            InvoiceEntity invoice = invoiceProduct.getInvoice();
            ProductOrServiceEntity product = invoiceProduct.getProduct();

            // Checks if invoice is already in the mao
            if (invoiceProductMap.containsKey(invoice)) {
                // If so, adds products to the list
                invoiceProductMap.get(invoice).add(product);
            } else {
                // If not, creates a new product List
                List<ProductOrServiceEntity> productList = new ArrayList<>();
                productList.add(product);
                invoiceProductMap.put(invoice, productList);
            }
        }

        return invoiceProductMap;
    }

    // Jauna klase, kur uzreiz ir invoice objekts un produktu liste
    public CurrentInvoice getCurrentInvoiceWithProducts(InvoiceEntity invoice) {
        List<InvoiceProductEntity> getAll = this.invoiceProductRepository.findAll();
        Map<InvoiceEntity, List<ProductOrServiceEntity>> productsInInvoice = getInvoiceAndProductMap(getAll);
        List<ProductOrServiceEntity> invoicedProducts =
                productsInInvoice.get(invoice);
        System.out.println(invoicedProducts);
        return new CurrentInvoice(invoice, invoicedProducts);
    }

}