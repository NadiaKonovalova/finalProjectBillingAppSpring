package com.finalprojectbillingapp.productOrService;

import com.finalprojectbillingapp.customer.CustomerEntity;
import com.finalprojectbillingapp.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    // delete product / service
}
