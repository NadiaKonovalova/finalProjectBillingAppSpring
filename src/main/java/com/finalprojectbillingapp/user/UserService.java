package com.finalprojectbillingapp.user;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

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

    @PersistenceContext
    private EntityManager entityManager;

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
    // Single user
    public UserEntity getUserById(UUID id) throws Exception {

        return this.userRepository.findById(id).orElseThrow();
    }

    public UserEntity showUserDetails (UserEntity user) throws Exception {
        for (UserEntity currentUser : this.getAllUsers()) {
            if(currentUser.getId().equals(user.getId())) {

                return currentUser;
            }
        }
        throw new Exception("Something went wrong");
    }

    @Transactional
    // Edit user profile
    public UserEntity editUserDetails(UserEntity user, UUID id) throws Exception {
        UserEntity currentUser = this.findUserById(id);
        try {
            if (currentUser.getId().equals(user.getId())) {
                currentUser.setName(user.getName());
                currentUser.setEmail(user.getEmail());
                currentUser.setTaxpayerNo(user.getTaxpayerNo());
                currentUser.setLegalAddress(user.getLegalAddress());
                currentUser.setBankName(user.getBankName());
                currentUser.setAccountNo(user.getAccountNo());
                currentUser.setCountry(user.getCountry());
                entityManager.flush();
            }

            return currentUser;
        } catch (OptimisticLockException exception){
            throw new Exception("Please refresh to activate changes");
        } catch (PersistenceException exception){
            throw new Exception("Database update failed.");
        }
        catch (Exception exception) {

            throw new Exception("Something went wrong");
        }
    }
// Show current user
    // Returns user data


    public UserEntity findUserById (UUID id)
            throws Exception{
        for (UserEntity currentUser: this.getAllUsers()) {
            if(currentUser.getId().equals(id))
                return currentUser;
        } throw new Exception("User not found");
    }

    // Delete user
    public void deleteUser(UUID id) throws Exception {
        if (!this.getAllUsers().removeIf((user) ->
                user.getId().equals(id)))
            throw new Exception("User with this ID not found");
    }
    // Edit user details
    // Log out
    // Add user profile
    // Delete user
}