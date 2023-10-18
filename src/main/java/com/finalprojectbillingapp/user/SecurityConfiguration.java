package com.finalprojectbillingapp.user;//package com.finalprojectbillingapp.user;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//    @Bean
//    public UserDetailsService userDetailsManager(){
//        UserDetails user = User.withUsername("email")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers
//                        (HttpMethod.valueOf("/mainPage/"))
//                .hasRole("USER")
//                .anyRequest()
//                .authenticated()
//                .requestMatchers("/mainPage/**").
//                authenticated() .
//                requestMatchers("/**") .
//                permitAll() .
//                and() .
//                formLogin() .
//                loginPage("/login") .
//                loginProcessingUrl("/login") .
//                defaultSuccessUrl("/mainPage");
//        return http.build();
//
//
//
////        http.authorizeRequests()
////                .requestMatchers("/login").permitAll()
////                .requestMatchers("/**").authenticated()
////                .and()
////                .formLogin().permitAll()
////                .loginPage("/login.html")
////                .loginProcessingUrl("/perform_login")
////                .defaultSuccessUrl("/homepage.html", true)
////                .failureUrl("/login.html?error=true")
////                .failureHandler(authenticationFailureHandler())
////                .and()
////                .logout()
////                .logoutUrl("/perform_logout")
////                .deleteCookies("JSESSIONID")
////                .logoutSuccessHandler(logoutSuccessHandler());
////        return http.build();
//    }
//
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new MyUserDetailsService();
//    }
//}
//
