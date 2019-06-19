package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.exception.NoStockException;
import com.esliceu.keep_it_safe.exception.StockOverflowException;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InvoiceManager {

    private InvoiceRepository invoiceRepository;
    private LuggageManager luggageManager;
    private UserManager userManager;
    private StockManager stockManager;

    public InvoiceManager(InvoiceRepository invoiceRepository, LuggageManager luggageManager,
                          UserManager userManager, StockManager stockManager) {
        this.invoiceRepository = invoiceRepository;
        this.luggageManager = luggageManager;
        this.userManager = userManager;
        this.stockManager = stockManager;
    }

    public List<Invoice> getAllInvoices() {
        return  (List<Invoice>) invoiceRepository.findAll();
    }

    public String invoicesToJSON(List<Invoice> invoices){

        StringBuilder invoicesToJSON = new StringBuilder();
        invoicesToJSON.append("[");

        for (Invoice invoice: invoices) {
            invoicesToJSON.append(invoice.stringToJSON() +",");
        }

        invoicesToJSON.deleteCharAt(invoicesToJSON.length()-1);
        invoicesToJSON.append("]");

        return invoicesToJSON.toString();
    }

    public void saveInvoice (Invoice invoice) throws NoStockException {

        System.out.println("THE INVOICE -->> "  + invoice.getStart_date().getTime() );
        User user = userManager.getUserByEmail(invoice.getUser().getEmail());
        invoice.setUser(user);
        stockManager.checkStock(invoice);
        invoiceRepository.save(invoice);
    }

    public void editInvoice(Invoice invoice) throws NoStockException {

        User user = userManager.getUserByEmail(invoice.getUser().getEmail());
        invoice.setUser(user);

        stockManager.recalculateStock(invoice);

        invoiceRepository.save(invoice);
    }

    public void deleteInvoice(int id_invoice) throws StockOverflowException {

        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id_invoice);

        Invoice invoice = invoiceOptional.get();
        stockManager.restoreStock(invoice);
        invoice.setUser(null);
        invoiceRepository.save(invoice);

        invoiceRepository.delete(invoice);
    }


    public Invoice getInvoiceById(int id) { return this.invoiceRepository.findById(id).orElse(null); }

    public List<Invoice> getInvoicesByUser(User user) {

        //Hacer una busqueda de las invoices por la id del usuario.
        return this.invoiceRepository.findByuser_userId(user.getUserId());

    }
}
