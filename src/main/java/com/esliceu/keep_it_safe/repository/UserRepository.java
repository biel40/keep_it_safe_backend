package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

     User findByEmail(String email);
     User findByEmailAndPassword(String email, String password);
     User findByInvoices(Invoice invoice);
     User findAllByUserId(long id);
}
