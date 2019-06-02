package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Luggage;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MainController {

    private final InvoiceRepository invoiceRepository;
    private final LuggageRepository luggageRepository;
    private final UserRepository userRepository;

    public MainController(LuggageRepository luggageRepository, UserRepository userRepository, InvoiceRepository invoiceRepository) {
        this.luggageRepository = luggageRepository;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /* LUGGAGES */

    @RequestMapping(value = "/luggages", method = RequestMethod.GET)
    public List<Luggage> getLuggages() {
        return (List<Luggage>) this.luggageRepository.findAll();
    }


    /* INVOICES */

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public List<Invoice> getAllInvoices() {
        return (List<Invoice>) this.invoiceRepository.findAll();
    }

    @RequestMapping(value = "/invoices/current", method = RequestMethod.GET)
    public List<Invoice> getAllCurrentInvoices() {

        List<Invoice> allInvoices = (List<Invoice>) this.invoiceRepository.findAll();
        LinkedList<Invoice> currentInvoices = new LinkedList<>();

        for (Invoice invoice : allInvoices ) {
            // Miramos que la fecha en la que acaba la factura NO sea inferior a la actual.
            if (!(invoice.getEnd_date().isBefore(Instant.now())))
                currentInvoices.add(invoice);
        }
        return currentInvoices;
    }


    @RequestMapping(value = "/luggages/price", method = RequestMethod.PUT)
    public ResponseEntity changePrices(@RequestBody List<Luggage> luggages){
        try {
            System.out.println(luggages.toString());
            luggageRepository.saveAll(luggages);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
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
