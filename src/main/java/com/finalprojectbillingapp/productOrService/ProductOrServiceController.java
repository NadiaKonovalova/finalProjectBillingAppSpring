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

    @GetMapping("/product-service")
    public String displayAllProducts(Model model) {
        List<ProductOrServiceEntity> productsOrServices = serviceForProducts.getAllProducts();
        model.addAttribute("productsOrServices", productsOrServices);
        return "allProductsAndServices";
    }

    @GetMapping("/add-product-service")
    public String displayAddProductService() {
        return "addProductsOrServices";
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
    public String displayEditProductService(@PathVariable UUID id, Model model) {
        try {
            ProductOrServiceEntity productOrServiceEntity = this.serviceForProducts.findProductOrServiceById(id);
            model.addAttribute("productOrService", productOrServiceEntity);
            return ""; // need to add name of the html page - display page where user can edit product/service item
        } catch (Exception exception) {
            return "redirect:/edit-product-service?status=PRODUCT_SERVICE_EDIT_FAILED&error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit-product-service/{id}")
    public String editProductService(@PathVariable UUID id, ProductOrServiceEntity productOrServiceEntity) {
        try {
            this.serviceForProducts.findProductOrServiceById(id);
            productOrServiceEntity.setId(id);
            this.serviceForProducts.editProductService(id, productOrServiceEntity);
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
            return "redirect:/delete?status=PRODUCT_SERVICE_DELETION_FAILED&error=" + exception.getMessage();
        }
    }

}
