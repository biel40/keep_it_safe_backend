package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

    List<Invoice> findByUser(User user);

    List<Invoice> findByuser_userId(long user_id);

}
