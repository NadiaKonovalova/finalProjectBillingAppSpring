package com.finalprojectbillingapp.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class CustomerController {
    private final com.finalprojectbillingapp.customer.CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer-list")
    public String displayAllCustomers(Model model) {
        List<com.finalprojectbillingapp.customer.CustomerEntity> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "allCustomers"; // need to add name of the html page - list of all customers
    }

    @GetMapping("/add-customer")
    public String displayAddCustomer(com.finalprojectbillingapp.customer.CustomerEntity customerEntity) {
        return "addCustomer"; // need to add name of the html page - display page where user can add customers
    }

    @PostMapping("/add-customer")
    public String createCustomer(com.finalprojectbillingapp.customer.CustomerEntity customerEntity) {
        try {
            customerService.createCustomer(customerEntity);
            return "redirect:/customer-list";
        } catch (Exception exception) {
            return "redirect:/add-customer?status=CUSTOMER_CREATION_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/edit-customer/{id}")
    public String displayEditCustomer(@PathVariable UUID id, Model model) {
        try {
            com.finalprojectbillingapp.customer.CustomerEntity customerEntity = this.customerService.findCustomerById(id);
            model.addAttribute("customer", customerEntity);
            return "editCustomer";
        } catch (Exception exception) {
            return "redirect:/edit-customer?status=CUSTOMER_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit-customer/{id}")
    public String editCustomer(@PathVariable UUID id, CustomerEntity customerEntity) {
        try {
            this.customerService.findCustomerById(id);
            customerEntity.setId(id);
            this.customerService.editCustomerDetails(id, customerEntity);
            return "redirect:/customer-list";
        } catch (Exception exception) {
            return "redirect:/edit-customer?status=CUSTOMER_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable UUID id) {
        try {
            customerService.deleteCustomer(id);
            return "redirect:/customer-list";
        } catch (Exception exception) {
            return "redirect:/delete?status=CUSTOMER_DELETION_FAILED&error=" + exception.getMessage();
        }
    }


}