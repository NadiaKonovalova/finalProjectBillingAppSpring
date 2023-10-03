package com.finalprojectbillingapp.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        public void createCustomer (CustomerEntity customerEntity)
                throws Exception {
            this.customerRepository.save(customerEntity);
        }

        // Edit customer details
    // Delete a customer
    // View all customers

}
