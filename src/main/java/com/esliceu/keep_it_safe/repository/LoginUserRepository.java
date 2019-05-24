package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.LoginUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginUserRepository extends MongoRepository<LoginUser, String> {

     LoginUser findByUserName(String username);

}
