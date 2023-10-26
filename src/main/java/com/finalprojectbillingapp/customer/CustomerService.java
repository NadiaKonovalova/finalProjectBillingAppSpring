package com.finalprojectbillingapp.customer;

import com.finalprojectbillingapp.invoice.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private InvoiceRepository invoiceRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           InvoiceRepository invoiceRepository){
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
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
        existingCustomerDetails.setCountry(updatedCustomerDetails.getCountry());

        customerRepository.save(existingCustomerDetails);
    }

    public CustomerEntity getCustomerById(UUID customerId) {
        return this.customerRepository.findById(customerId).orElseThrow();
    }

    public List<CustomerEntity> getAllCustomerByUserLoginEmail (String email) {
        return this.invoiceRepository.findCustomersByUserLoginEmail(email);
    }
}