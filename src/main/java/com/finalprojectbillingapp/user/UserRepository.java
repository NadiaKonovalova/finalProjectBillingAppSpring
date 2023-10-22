package com.finalprojectbillingapp.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRepository extends
        CrudRepository<UserEntity, UUID> {
    UserEntity findByLoginEmailAndPassword
        (String loginEmail, String password);

    UserEntity findByLoginEmail (String loginEmail);

/*    @Override
    List<UserEntity> findAll();*/

}
