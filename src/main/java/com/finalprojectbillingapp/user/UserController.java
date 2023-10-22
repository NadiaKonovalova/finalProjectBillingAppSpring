package com.finalprojectbillingapp.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@SessionAttributes("userData")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    @Autowired
    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @GetMapping("/login")
    public String displayLogPage(){
        return "login";
    }

    @PostMapping("/login")
    public String userLoginForm(LoginRequest loginRequest,
                                  HttpServletResponse response){
        try {
            UserEntity user = this.userService.verifyLogin
                    (loginRequest.getLoginEmail(), loginRequest.getPassword());
            if(user==null) throw new Exception("User not found or credentials are incorrect");
                    Cookie cookie = new Cookie("loggedInUserId",
                            user.getId().toString());
                    cookie.setMaxAge(200000);
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                    return "redirect:/mainPage";

        } catch (Exception exception) {
            return "redirect:/login?status=LOGIN_FAILED&error="
                    + exception.getMessage();
        }
    }
//@GetMapping("/create-new-account")
//public String displayCreateNewAccountPage(Model model) {
///*    model.addAttribute("loginEmail");
//    model.addAttribute("password");*/
//    return "createNewAccount";
//}

//@PostMapping("/create-new-account")
//public String createNewAccount(LoginRequest loginRequest,
//                               Model model) throws Exception {
//    try {
//        String loginEmail = loginRequest.getLoginEmail();
//
//        if (this.userService.checkIfAccountExists(loginEmail)) {
//            model.addAttribute("error", "Creating an account failed: " +
//                    "account already exists.");
//            return "redirect:/login?status=ACCOUNT_EXISTS";
//        }
//            UserEntity user = new UserEntity();
//            this.userService.createUser(user);
//            user.setLoginEmail(loginEmail);
//            user.setPassword(loginRequest.getPassword());
//            user.setContactEmail(loginEmail);
//
//            return "redirect:/register";
//        }   catch (Exception exception) {
//        model.addAttribute("error", "Creating an account failed: "
//                + exception.getMessage());
//        return "createNewAccount";
//    }
//}

@GetMapping("/logout")

    public String logOutPage(@CookieValue (value=
            "loggedInUserId", defaultValue = "") String userId,
                               HttpServletResponse response){
        Cookie userCookie = new Cookie("loggedInUserId", null);
        userCookie.setMaxAge(0); // expires now = is deleted
        response.addCookie(userCookie); // Adds new cookie

        return "redirect:/login?status=LOGOUT_SUCCESSFUL";
    }

    @GetMapping("/register")
    public String displayRegisterPage(){
        return "register";
    }

    @PostMapping("/register")
    public String handleUserRegistration(UserEntity userEntity,
                                         HttpServletRequest request,
                                         LoginRequest loginRequest,
                                         Model model) {
        try {
            System.out.println(userEntity);
            userEntity.setEmail(userEntity.getLoginEmail());
            this.userService.createUser(userEntity);
//            this.userRepository.save(userEntity);
                return "redirect:/login?status=REGISTRATION_SUCCESS";
        } catch(Exception exception){
            return "redirect:/register?status=REGISTRATION_FAILED&?error="
                    + exception.getMessage();
        }
    }

        @GetMapping("/userProfile")
    public String displayUserProfilePage(){

        return "userProfile";
    }

    @GetMapping("/singleUserProfile")
    public String displaySingleUser(Model model, HttpServletRequest request) throws Exception {
        String userId = CookieHandling.getUserIdFromCookies(request);

        if (userId != null) {
            // Use 'userId' to fetch and display the user's data in the view
            UserEntity user = this.userService.getUserById(UUID.fromString(userId));
            model.addAttribute("user", user);
            return "singleUserProfile"; // Return the view for displaying the user's profile
        } else {
            // Handle the case where the cookie is missing or invalid
            return "redirect:/login"; // Redirect to the login page or another appropriate page
        }
    }
//    @GetMapping("/singleUserProfile/{id}")
//    public String displaySingleUser(@CookieValue(value="loggedInUserId")
//                                        @PathVariable("id") String userId, Model model) {
//        try {
//            if(userId.isBlank()) throw new RuntimeException("User not found");
//            model.addAttribute("user", this.userService.getUserById
//                    (fromString(userId)));
//            model.addAttribute("userProfile", this.userService.getAllUsers());
//            return "singleUserProfile";
//        } catch (Exception exception) {
//            return "redirect:/?message=USER_PROFILE_NOT_FOUND&error=" + exception.getMessage();
//        }
//    }
//    @GetMapping("/userProfile/{id}")
//    public String displaySingleUser(@PathVariable("id") UUID userId,
//                                    Model model){
//        try {
//            UserEntity user = this.userService.getUserById(userId);
//            if(user==null){
//                return "redirect:/?message=USER_PROFILE_NOT_FOUND";
//            }
//            model.addAttribute("user", user);
//// also catch UserNotFound custom Exc
//            return "userProfile" + user.getId();
//        } catch (Exception exception) {
//            return "redirect:/?message=USER_PROFILE_NOT_FOUND&error=" + exception.getMessage();
//        }
//    }
       @GetMapping("/mainPage")
    public String displayMainPage(){
        return "mainPageForUser";
    }

    @GetMapping("/editUser")
    public String displayEditUserPage(HttpServletRequest request, Model model) {
        String userId = CookieHandling.getUserIdFromCookies(request);
        try {
            if(userId != null) {
                UserEntity users = this.userService.findUserById(UUID.fromString(userId));
                model.addAttribute("user", users);
            }
            return "editUser";
        } catch (Exception exception) {
            return "redirect:/?message=EDITING_USER_FAILED&error="
                    + exception.getMessage();
        }
    }

    @PostMapping("/editUser/{id}")
    public String editUser(HttpServletRequest request, UserEntity user,
                           RedirectAttributes redirectAttributes){
        String userId = CookieHandling.getUserIdFromCookies(request);
                try {
                    if(userId != null) {
                        this.userService.findUserById(UUID.fromString(userId));
                        user.setId(UUID.fromString(userId));
                        UserEntity updatedUser = this.userService.editUserDetails
                                (user, UUID.fromString(userId));
                    }
                    redirectAttributes.addFlashAttribute("message",
                            "User updated successfully");
                        return "redirect:/singleUserProfile";

        } catch (Exception exception) {
                    redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/editUser";
        }
    }

    @GetMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request, UserEntity user){
        String userId = CookieHandling.getUserIdFromCookies(request);
        try {
            if(userId != null) {
                this.userService.deleteUser(UUID.fromString(userId));
            }
            return "redirect:/?message=USER_DELETED_SUCCESSFULLY";
        } catch (Exception exception){
            return "redirect:/?message=USER_DELETE_FAILED&error=" + exception.getMessage();
        }

    }

//    @GetMapping("/userProfile/{id}")
//    public String displaySingleUser(@PathVariable UUID id,
//                                    Model model){
//        try {
//            UserEntity singleUser = this.userService.findUserById(id);
//            model.addAttribute("user",singleUser);
//
//            return "redirect:/?message=USER_PROFILE/" + singleUser.getName();
//        } catch (Exception exception) {
//            return "redirect:/?message=USER_PROFILE_NOT_FOUND&error=" + exception.getMessage();
//        }
//    }
}