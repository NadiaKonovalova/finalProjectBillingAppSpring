package com.finalprojectbillingapp.productOrService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductOrServiceController {

    private final ServiceForProducts serviceForProducts;

    @Autowired
    public ProductOrServiceController(ServiceForProducts serviceForProducts) {
        this.serviceForProducts = serviceForProducts;
    }

    @GetMapping("/product-service")
    public String getAllProducts(Model model) {
        List<ProductOrServiceEntity> productsOrService = serviceForProducts.getAllProducts();
        model.addAttribute("productsOrService", productsOrService);
        return ""; // need to add name of the html page - list of all products and services
    }

    @GetMapping("/add-product-service")
    public String displayAddProductService() {
        return ""; // / need to add name of the html page - display page where user can add product or service
    }

    @PostMapping("/add-product-service")
    public String createProductService() {

    }

}
