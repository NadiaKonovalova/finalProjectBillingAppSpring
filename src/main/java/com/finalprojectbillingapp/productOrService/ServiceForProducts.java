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
    public ServiceForProducts(ProductServiceRepository productServiceRepository){
        this.productServiceRepository = productServiceRepository;
    }
    // Returns all products saved to the DB
    public List<ProductOrServiceEntity> getAllProducts(){
        return (ArrayList<ProductOrServiceEntity>)
                this.productServiceRepository.findAll();
    }
    // To create a new product or service
    public void createProductService (ProductOrServiceEntity productOrServiceEntity)
            throws Exception {
        this.productServiceRepository.save(productOrServiceEntity);
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
        ProductOrServiceEntity productOrServiceEntity = productServiceRepository.findById(id)
                .orElseThrow(() -> new Exception("Product or service not found with ID: " + id));

        productServiceRepository.delete(productOrServiceEntity);
    }

}