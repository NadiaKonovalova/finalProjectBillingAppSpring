package com.finalprojectbillingapp.user;

import com.finalprojectbillingapp.invoice.InvoiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
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
    private CookieHandling cookieHandling;
    private InvoiceRepository invoiceRepository;

    @Autowired
    // Constructor
    public UserService(UserRepository userRepository,
                       InvoiceRepository invoiceRepository){
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public boolean checkIfAccountExists (String loginEmail) {
        UserEntity user = this.userRepository.findByLoginEmail
                (loginEmail);
        if (user == null) {
            return false;
        } else {
            return true;
        }
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
            (String loginEmail, String password)
        throws Exception {
        UserEntity user = this.userRepository.findByLoginEmailAndPassword
                (loginEmail, password);
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

    public UserEntity editUserDetailsForInvoice(UserEntity user, UUID id) throws Exception {
        UserEntity currentUser = this.findUserById(id);
        try {
            if (currentUser.getId().equals(user.getId())) {
                currentUser.setName(user.getName());
                currentUser.setEmail(user.getEmail());
                currentUser.setTaxpayerNo(user.getTaxpayerNo());
                currentUser.setLegalAddress(user.getLegalAddress());
                currentUser.setTaxpayerType(user.getTaxpayerType());
                currentUser.setBankName(user.getBankName());
                currentUser.setAccountNo(user.getAccountNo());
                currentUser.setCountry(user.getCountry());
                currentUser = userRepository.save(currentUser);
                return currentUser;
            } else {
                throw new Exception("User not found");
            }
        } catch (Exception exception) {

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

    public UserEntity getLoggedInUser(HttpServletRequest request) throws Exception {
        String cookieId = CookieHandling.getUserIdFromCookies(request);
                try {
                    UserEntity loggedInUser = this.getUserById(UUID.fromString(cookieId));
                    return loggedInUser;
                } catch (Exception exception){
                    throw new Exception("User not found ");
                }
    }

    @Transactional
    // Edit user profile
    public Type editTaxPayerType(UserEntity user, UUID id) throws Exception {
        UserEntity currentUser = this.findUserById(id);
        try {
            if (currentUser.getId().equals(user.getId())) {
                currentUser.setTaxpayerType(user.getTaxpayerType());
                entityManager.flush();
            }
            return currentUser.getTaxpayerType();
        } catch (PersistenceException exception){
            throw new Exception("Database update failed.");
        }
        catch (Exception exception) {
            throw new Exception("Something went wrong");
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
