package com.finalprojectbillingapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Stores all user accounts
    public List<UserEntity> getAllUsers(){
        return (ArrayList<UserEntity>)
                this.userRepository.findAll();
    }
    // To create a new user account
    public void createUser (UserEntity userEntity)
        throws Exception {
        this.userRepository.save(userEntity);
    }
// Log in after sign-up; might be improved to check if an e-mail is even registeted
    public UserEntity verifyLogin
            (String email, String password)
        throws Exception {
        UserEntity user = this.userRepository.findByEmailAndPassword
                (email, password);
        return user;
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
