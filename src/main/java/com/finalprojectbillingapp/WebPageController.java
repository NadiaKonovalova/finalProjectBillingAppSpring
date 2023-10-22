package com.finalprojectbillingapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {
    @GetMapping("/")
    public String displayHomePage(){
        return "index";
    }
    @GetMapping("/about")
    public String displayAboutPage(){
        return "about";
    }

    @GetMapping("/contactUs")
    public String displayContactPage(){
        return "contactUs";
    }
//    @PostMapping("/contact")
//    public String createMessageToUs(){
//        return ""
//
//    }


}