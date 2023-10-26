package com.finalprojectbillingapp.customer;

import com.finalprojectbillingapp.invoice.InvoiceRepository;
import com.finalprojectbillingapp.productOrService.ProductOrServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private InvoiceRepository invoiceRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public List<CustomerEntity> getAllCustomers(){
        return (ArrayList<CustomerEntity>)
                this.customerRepository.findAll();
    }


    public void createCustomer (CustomerEntity customerEntity) {
        this.customerRepository.save(customerEntity);
    }

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

    public CustomerEntity getCustomerById(UUID customerId) throws Exception {
        return this.customerRepository.findById(customerId).orElseThrow();
    }
    public List<CustomerEntity> getAllCustomerByUserLoginEmail (String email) throws Exception {
        return this.invoiceRepository.findCustomersByUserLoginEmail(email);
    }
}