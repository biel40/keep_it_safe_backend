package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}