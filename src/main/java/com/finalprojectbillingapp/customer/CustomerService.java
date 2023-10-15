package com.finalprojectbillingapp.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    // Returns all customers saved to the DB
    public List<CustomerEntity> getAllCustomers(){
        return (ArrayList<CustomerEntity>)
                this.customerRepository.findAll();
    }

    // To create a new customer instance
    public void createCustomer (CustomerEntity customerEntity) {
        this.customerRepository.save(customerEntity);
    }

    // Edit customer details
    public void editCustomerDetails(UUID id, CustomerEntity updatedCustomerDetails) throws Exception {
        CustomerEntity existingCustomerDetails = customerRepository.findById(id)
                .orElseThrow(() -> new Exception("Customer not found with ID: " + id));

        existingCustomerDetails.setName(updatedCustomerDetails.getName());
        existingCustomerDetails.setTaxpayerNo(updatedCustomerDetails.getTaxpayerNo());
        existingCustomerDetails.setLegalAddress(updatedCustomerDetails.getLegalAddress());
        existingCustomerDetails.setBankName(updatedCustomerDetails.getBankName());
        existingCustomerDetails.setAccountNo(updatedCustomerDetails.getAccountNo());
        existingCustomerDetails.setTaxpayerType(updatedCustomerDetails.getTaxpayerType());
        existingCustomerDetails.setCountry(updatedCustomerDetails.getCountry());

        customerRepository.save(existingCustomerDetails);
    }


    // Delete a customer
    public void deleteCustomer(UUID id) throws Exception {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new Exception ("Customer not found with ID: " + id));

        customerRepository.delete(customerEntity);
    }

    public CustomerEntity findCustomerById(UUID id) throws Exception {
        for (CustomerEntity customerEntity: this.customerRepository.findAll()) {
            if (customerEntity.getId().equals(id))return customerEntity;
        }
        throw new Exception("Customer not found");
    }

}