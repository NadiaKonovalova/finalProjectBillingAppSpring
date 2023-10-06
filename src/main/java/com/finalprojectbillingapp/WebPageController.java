package com.finalprojectbillingapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebPageController {
    @GetMapping("")
    public String displayHomePage(){
        return "index";
    }
    @GetMapping("/about")
    public String displayAboutPage(){
        return "about";
    }
   /* @GetMapping("/login")
    public String displayLogInPage(){
        return "login";
    } */
    //not here, but in user controller
    /*@GetMapping("/contact")
    public String displayContactPage(){
        return "contact";
    }
    @PostMapping("/contact")
    public String createMessageToUs(){

    }*/


}
