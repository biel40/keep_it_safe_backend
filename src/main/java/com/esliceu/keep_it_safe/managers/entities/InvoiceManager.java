package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InvoiceManager {

    private InvoiceRepository invoiceRepository;
    private LuggageManager luggageManager;

    @Autowired
    public InvoiceManager(InvoiceRepository invoiceRepository, LuggageManager luggageManager) {
        this.invoiceRepository = invoiceRepository;
        this.luggageManager = luggageManager;
    }

    public List<Invoice> getAllInvoices() {
        return  (List<Invoice>) invoiceRepository.findAll();
    }

    public String invoicesToJSON(List<Invoice> invoices){

        StringBuilder invoicesToJSON = new StringBuilder();
        invoicesToJSON.append("[");

        for (Invoice invoice: invoices) {
            invoicesToJSON.append(invoice.toString() +",");
        }

        invoicesToJSON.deleteCharAt(invoicesToJSON.length()-1);
        invoicesToJSON.append("]");

        return invoicesToJSON.toString();
    }

    public Invoice getInvoiceById(int id) { return this.invoiceRepository.findById(id).orElse(null); }
}
