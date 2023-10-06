package com.finalprojectbillingapp.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    // Constructor
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Stores all user accounts in ArrayList
    public List<UserEntity> getAllUsers(){
        return (ArrayList<UserEntity>)
                this.userRepository.findAll();
    }
    // To create a new user account
    public void createUser (UserEntity userEntity)
        throws Exception {
        this.userRepository.save(userEntity);
    }
// Log in after sign-up;
// might be improved to check if an e-mail is even registered
    // Password validity / security requirements to be added
    public UserEntity verifyLogin
            (String email, String password)
        throws Exception {
        UserEntity user = this.userRepository.findByEmailAndPassword
                (email, password);
        return user;
    }

    // Edit user profile
    public UserEntity editUserDetails(UserEntity user) throws Exception {
        for (UserEntity currentUser : this.getAllUsers()) {
            if(currentUser.getId().equals(user.getId())) {
                currentUser.setName(user.getName());
                currentUser.setEmail(user.getEmail());
                currentUser.setTaxpayerNo(user.getTaxpayerNo());
                currentUser.setLegalAddress(user.getLegalAddress());
                currentUser.setBankName(user.getBankName());
                currentUser.setAccountNo(user.getAccountNo());
                currentUser.setCountry(user.getCountry());
                return currentUser;
            }
        }
        throw new Exception("Something went wrong");
    }
// Show current user
    public UserEntity findUserById (Long id) throws Exception{
        for(UserEntity singleUser: this.getAllUsers()){
            if(singleUser.getId().equals(id))
                return singleUser;
        }
        throw new Exception("Something went wrong");
    }

    // Returns user data
    public UserEntity findUserById (UUID id)
        throws Exception{
        for (UserEntity currentUser: this.getAllUsers()) {
            if(currentUser.getId().equals(id))
                return currentUser;
        } throw new Exception("User not found");
    }

    // Edit user details
    // Log out
}
