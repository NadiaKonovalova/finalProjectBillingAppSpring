package com.finalprojectbillingapp.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String displayHomePage(){
        return "home";
    }
    @GetMapping("/login")
    public String displayLogPage(){
        return "login";
    }

    @PostMapping("/login")
    public String userLoginForm(LoginRequest loginRequest,
                                  HttpServletResponse response){
        try {
            // Checs if user exists
            UserEntity user = this.userService.verifyLogin
                    (loginRequest.getEmail(), loginRequest.getPassword());
            if(user==null) return "redirect:/register";
//            throw new Exception("E-mail or password is incorrect");
            // Create cookie
            Cookie cookie = new Cookie("loggedInUserId",
                    user.getId().toString());
            // create cookie expiry period in sec before deletion
            cookie.setMaxAge(200000);
            // save cookie to the HTTP request to enable
            // storing it in user's browser
            response.addCookie(cookie);

            return "redirect:/mainPage";
        } catch (Exception exception) {
            return "redirect:/login?status=LOGIN_FAILED&error="
                    + exception.getMessage();
        }
    }
    @GetMapping("/register")
    public String displayRegisterPage(){
        return "register";
    }

    @PostMapping("/register")
    public String handleUserRegistration(UserEntity userEntity) {
        System.out.println(userEntity);
        try {
            this.userService.createUser(userEntity);
            return "redirect:/login?status=REGISTRATION_SUCCESS";
        } catch (Exception exception) {
            return "redirect:/register?status=REGISTRATION_FAILED&?error="
                    + exception.getMessage();
        }
    }

    @GetMapping("/userProfile/{id}")
    public String displayCurrentUser(@PathVariable UUID id,
                                    Model model){
        try {
            UserEntity currentUser = this.userService.findUserById(id);
            model.addAttribute("user", currentUser);

            return "redirect:/?message=USER_PROFILE/" + currentUser.getName();
        } catch (Exception exception) {
            return "redirect:/?message=USER_PROFILE_NOT_FOUND&error=" + exception.getMessage();
        }
    }

    @GetMapping("/mainPage")
    public String displayMainPage(){
        return "mainPage";
    }
    @GetMapping("/editUser/{id}")
    public String displayEditUserPage(@PathVariable UUID id, Model model) {
        try {
            UserEntity user = this.userService.findUserById(id);
            model.addAttribute("currentUser", user);
            return "userProfile";
        } catch (Exception exception) {
            return "redirect:/?message=EDITING_USER_FAILED&error="
                    + exception.getMessage();
        }
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable UUID id, UserEntity user){
        try {
            this.userService.findUserById(id);
            user.setId(id);
            this.userService.editUserDetails(user);
            return "redirect:/?message=USER_EDITED_SUCCESSFULLY";
        } catch (Exception exception) {
            return "redirect:/?message=USER_EDITING_FAILED&errors=" +
                    exception.getMessage();
        }
    }
}