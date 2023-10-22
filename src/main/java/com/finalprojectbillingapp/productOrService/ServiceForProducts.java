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

    // Returns all products saved to the DB
    public List<ProductOrServiceEntity> getAllProducts() {
        return (ArrayList<ProductOrServiceEntity>)
                this.productServiceRepository.findAll();
    }

    // To create a new product or service
    public ProductOrServiceEntity createProductService(ProductOrServiceEntity productOrServiceEntity)
            throws Exception {
        this.productServiceRepository.save(productOrServiceEntity);
        return productOrServiceEntity;
    }

    // Edit product / service
    public void editProductService(UUID id, ProductOrServiceEntity updatedProductOrService) throws Exception {
        ProductOrServiceEntity existingProductOrService = productServiceRepository.findById(id)
                .orElseThrow(() -> new Exception("Product or service not found with ID: " + id));

        existingProductOrService.setName(updatedProductOrService.getName());
        existingProductOrService.setQuantity(updatedProductOrService.getQuantity());
        existingProductOrService.setUnit(updatedProductOrService.getUnit());
        existingProductOrService.setUnitPrice(updatedProductOrService.getUnitPrice());
        existingProductOrService.setCurrency(updatedProductOrService.getCurrency());
        existingProductOrService.setVATrate(updatedProductOrService.getVATrate());

        productServiceRepository.save(existingProductOrService);
    }

    // delete product / service
    public void deleteProductService(UUID id) throws Exception {
        ProductOrServiceEntity productOrServiceEntity = productServiceRepository
                .findById(id).orElseThrow(() -> new Exception("Product or service not found with ID: " + id));

        productServiceRepository.delete(productOrServiceEntity);
    }

    public ProductOrServiceEntity getProductById(UUID productID) throws Exception {
        return this.productServiceRepository.findById(productID).orElseThrow();
    }

    public ProductOrServiceEntity findProductOrServiceById(UUID id) throws Exception {
        for (ProductOrServiceEntity productOrServiceEntity : this.productServiceRepository.findAll()) {
            if (productOrServiceEntity.getId().equals(id)) return productOrServiceEntity;
        }
        throw new Exception("Product or service not found");
    }

    public double calculatePricePerProduct(ProductOrServiceEntity productOrServiceEntity) {
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
        double totalPrice = price * quantity * (1 + taxRate);

        return totalPrice;
    }
}