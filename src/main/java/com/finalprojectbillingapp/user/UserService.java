package com.finalprojectbillingapp.user;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean checkIfAccountExists (String loginEmail) {
        UserEntity user = this.userRepository.findByLoginEmail
                (loginEmail);
        return (user != null);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserEntity> getAllUsers(){
        return (ArrayList<UserEntity>)
                this.userRepository.findAll();
    }

    public void createUser (UserEntity userEntity)
        throws Exception {
        this.userRepository.save(userEntity);
    }

    public UserEntity verifyLogin
            (String loginEmail, String password) {
        UserEntity user = this.userRepository.findByLoginEmailAndPassword
                (loginEmail, password);
        return user;
    }

    public UserEntity getUserById(UUID id) throws Exception {
        return this.userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public UserEntity editUserDetails(UserEntity user, UUID id) throws Exception {
        UserEntity currentUser = this.findUserById(id);
        try {
            if (currentUser.getId().equals(user.getId())) {
                currentUser.setName(user.getName());
                currentUser.setTaxpayerNo(user.getTaxpayerNo());
                currentUser.setLegalAddress(user.getLegalAddress());
                currentUser.setTaxpayerType(user.getTaxpayerType());
                currentUser.setBankName(user.getBankName());
                currentUser.setAccountNo(user.getAccountNo());
                currentUser.setCountry(user.getCountry());
                entityManager.merge(currentUser);
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

    public UserEntity findUserById (UUID id)
        throws Exception{
        for (UserEntity currentUser: this.getAllUsers()) {
            if(currentUser.getId().equals(id))
                return currentUser;
        } throw new Exception("User not found");
    }

    public UserEntity getLoggedInUser(HttpServletRequest request) throws Exception {
        String cookieId = CookieHandling.getUserIdFromCookies(request);
                try {
                    UserEntity loggedInUser = this.getUserById(UUID.fromString(cookieId));
                    return loggedInUser;
                } catch (Exception exception){
                    throw new Exception("User not found ");
                }
    }


    public String getLoggedInUserEmail(HttpServletRequest request) throws Exception {
        try {
            UserEntity user = getLoggedInUser(request);
            if (!(user == null)){
                return user.getLoginEmail();
            } else {
                return null;
            }
        } catch (Exception exception){
            throw new Exception("User not found");
        }

    }
}
