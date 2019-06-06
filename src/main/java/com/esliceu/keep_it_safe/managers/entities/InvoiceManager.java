package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class InvoiceManager {

    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceManager(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAllInvoices() {
        return (List<Invoice>) invoiceRepository.findAll();
    }
}
