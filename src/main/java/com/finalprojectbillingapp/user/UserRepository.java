package com.finalprojectbillingapp.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface UserRepository extends
        CrudRepository<UserEntity, UUID> {
    UserEntity findByEmailAndPassword
        (String email, String password);


}
