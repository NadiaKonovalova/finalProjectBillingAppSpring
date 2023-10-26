package com.finalprojectbillingapp.productOrService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceForProducts {
    private ProductServiceRepository productServiceRepository;

    @Autowired
    public ServiceForProducts(ProductServiceRepository productServiceRepository) {
        this.productServiceRepository = productServiceRepository;
    }

    public List<ProductOrServiceEntity> getAllProducts() {
        return (ArrayList<ProductOrServiceEntity>)
                this.productServiceRepository.findAll();
    }

    public ProductOrServiceEntity createProductService(ProductOrServiceEntity productOrServiceEntity)
            throws Exception {
        this.productServiceRepository.save(productOrServiceEntity);
        return productOrServiceEntity;
    }

    public ProductOrServiceEntity getProductById(UUID productID) throws Exception {
        return this.productServiceRepository.findById(productID).orElseThrow();
    }

    public double calculatePricePerProductWithVAT(ProductOrServiceEntity productOrServiceEntity) {
        double price = productOrServiceEntity.getUnitPrice();
        Category taxCategory = productOrServiceEntity.getVATrate();
        double taxRate = 0.0;
        if (taxCategory.equals(Category.REDUCED5)) {
            taxRate = 0.05;
        } else if (taxCategory.equals(Category.REDUCED10)) {
            taxRate = 0.1;
        } else if (taxCategory.equals(Category.REDUCED12)) {
            taxRate = 0.12;
        } else if (taxCategory.equals(Category.STANDARD21)) {
            taxRate = 0.21;
        }
        double quantity = productOrServiceEntity.getQuantity();
        double totalPriceWithVAT = price * quantity * (1 + taxRate);;

        return totalPriceWithVAT;
    }

    public double calculatePricePerProductNoVAT(ProductOrServiceEntity productOrServiceEntity) {
        double price = productOrServiceEntity.getUnitPrice();
        Category taxCategory = productOrServiceEntity.getVATrate();
        double quantity = productOrServiceEntity.getQuantity();
        double totalPrice = price * quantity;

        return totalPrice;
    }
}