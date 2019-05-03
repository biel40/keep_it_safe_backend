package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {
}
