//package com.finalprojectbillingapp.user;
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//public class MyUserDetailsService implements UserDetailsService {
//private final LoginRequest loginRequest = new LoginRequest();
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if ("user".equalsIgnoreCase(loginRequest.getEmail())) {
//            return User.builder().username(loginRequest.getEmail())
//                    .password(loginRequest.getPassword())
//                    .roles("USER")
//                    .build();
//        } else {
//            throw new UsernameNotFoundException("Email or password is incorrect");
//        }
//
//    }
//}
