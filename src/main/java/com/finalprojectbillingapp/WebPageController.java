package com.finalprojectbillingapp;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
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
    @GetMapping("/contactUs")
    public String displayContactPage(){
        return "contactUs";
    }
    /*@PostMapping("/contact")
    public String createMessageToUs(){

    }*/
  //  @GetMapping("/logout")
  //  public String logOutDisplayIndexPage(){
  //      return "redirect:/index";}



}
