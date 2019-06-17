package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

    List<Invoice> findByUser(User user);

    // @Query("from Invoice r INNER JOIN fetch r.user where r.user.user_id = :id")
    // @EntityGraph(value = "Invoice.user", type = EntityGraph.EntityGraphType.LOAD)

    List<Invoice> findByuser_userId(long user_id);

}
