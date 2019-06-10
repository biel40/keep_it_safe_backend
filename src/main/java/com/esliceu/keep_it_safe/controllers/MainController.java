package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Luggage;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.managers.entities.InvoiceManager;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
@RestController
public class MainController {

    private final InvoiceManager invoiceManager;
    private final LuggageRepository luggageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MainController(LuggageRepository luggageRepository, UserRepository userRepository, InvoiceManager invoiceManager) {
        this.luggageRepository = luggageRepository;
        this.userRepository = userRepository;
        this.invoiceManager = invoiceManager;
    }

    /* LUGGAGES */

    @RequestMapping(value = "/luggages", method = RequestMethod.GET)
    public List<Luggage> getLuggages() {
        return (List<Luggage>) this.luggageRepository.findAll();
    }

    @RequestMapping(value = "/luggages", method = RequestMethod.POST)
    public void FOO(@RequestBody     Luggage luggage) {

    }



    @RequestMapping(value = "/luggages/price", method = RequestMethod.PUT)
    public ResponseEntity changePrices(@RequestBody List<Luggage> luggages){
        try {
            luggageRepository.saveAll(luggages);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }


    /* INVOICES */

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public String getAllInvoices() {
        return invoiceManager.invoicesToJSON(invoiceManager.getAllInvoices());
    }

    @RequestMapping(value = "/invoices/current", method = RequestMethod.GET)
    public String getAllCurrentInvoices() {

        List<Invoice> allInvoices = this.invoiceManager.getAllInvoices();
        List<Invoice> currentInvoices = new LinkedList<>();
        Calendar now = Calendar.getInstance();
        for (Invoice invoice : allInvoices ) {
            // Miramos que la fecha en la que acaba la factura NO sea inferior a la actual.
            if (invoice.getEnd_date().after(now)){
                currentInvoices.add(invoice);
            }
        }
        return invoiceManager.invoicesToJSON(currentInvoices);
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }


}
