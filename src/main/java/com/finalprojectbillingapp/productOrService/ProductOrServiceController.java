package com.finalprojectbillingapp.productOrService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class ProductOrServiceController {

    private final ServiceForProducts serviceForProducts;

    @Autowired
    public ProductOrServiceController(ServiceForProducts serviceForProducts) {
        this.serviceForProducts = serviceForProducts;
    }

  /*  @GetMapping("/product-service")
    public String getAllProducts(Model model) {
        List<ProductOrServiceEntity> productsOrService = serviceForProducts.getAllProducts();
        model.addAttribute("productsOrService", productsOrService);
        return ""; // need to add name of the html page - list of all products and services
    }

    @GetMapping("/add-product-service")
    public String displayAddProductService() {
        return ""; // need to add name of the html page - display page where user can add product or service
    }

    @PostMapping("/add-product-service")
    public String createProductService(ProductOrServiceEntity productOrServiceEntity) {
        try {
            serviceForProducts.createProductService(productOrServiceEntity);
            return "redirect:/product-service";
        } catch (Exception exception) {
            return "redirect:/add-product-service?status=PRODUCT_SERVICE_CREATION_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/edit-product-service/{id}")
    public String displayEditProductService() {
        return ""; // need to add name of the html page - display page where user can edit product/service item
    }

    @PostMapping("/edit-product-service/{id}")
    public String editProductService(@PathVariable UUID id, @ModelAttribute ProductOrServiceEntity updatedProductOrService) {
        try {
            serviceForProducts.editProductService(id, updatedProductOrService);
            return "redirect:/product-service";
        } catch (Exception exception) {
            return "redirect:/edit-product-service?status=PRODUCT_SERVICE_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProductService(@PathVariable UUID id) {
        try {
            serviceForProducts.deleteProductService(id);
            return "redirect:/product-service";
        } catch (Exception exception) {
            return "redirect:/product-service?status=PRODUCT_SERVICE_DELETION_FAILED&error=" + exception.getMessage();
        }
    }*/

}
