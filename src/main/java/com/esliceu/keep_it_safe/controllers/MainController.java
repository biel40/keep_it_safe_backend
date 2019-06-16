package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.entities.Comment;
import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Luggage;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.managers.entities.CommentManager;
import com.esliceu.keep_it_safe.managers.entities.InvoiceManager;
import com.esliceu.keep_it_safe.managers.entities.UserManager;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MainController {

    private final InvoiceManager invoiceManager;
    private final LuggageRepository luggageRepository;
    private final UserManager userManager;
    private final CommentManager commentManager;

    @Autowired
    public MainController(LuggageRepository luggageRepository, UserManager userManager, InvoiceManager invoiceManager, CommentManager commentManager) {
        this.luggageRepository = luggageRepository;
        this.userManager = userManager;
        this.invoiceManager = invoiceManager;
        this.commentManager = commentManager;
    }

    /* LUGGAGES */

    @RequestMapping(value = "/luggages", method = RequestMethod.GET)
    public List<Luggage> getLuggages() {
        return (List<Luggage>) this.luggageRepository.findAll();
    }

    @RequestMapping(value = "/luggages", method = RequestMethod.POST)
    public void FOO(@RequestBody Luggage luggage) {

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


    @RequestMapping(value = "/invoice/user/notVerified", method = RequestMethod.GET)
    public String getClientReservations(@RequestBody User user) {

        System.out.println(user);

        List<Invoice> allInvoicesFromClient = this.invoiceManager.getInvoicesByUser(user);
        List<Invoice> filteredInvoices = new LinkedList<>();

        for(Invoice invoice : allInvoicesFromClient) {
            if(!invoice.isVerified()) {
                filteredInvoices.add(invoice);
            }
        }

        return invoiceManager.invoicesToJSON(filteredInvoices);

    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public ResponseEntity saveInvoice(@RequestBody Invoice invoice) {
        System.out.println("THE USER ->> " + invoice.getUser());
        User user = userManager.getUserByEmail(invoice.getUser().getEmail());

        invoiceManager.saveInvoice(invoice);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.DELETE)
    public ResponseEntity deleteInvoice(@RequestParam int id_invoice) {
        System.out.println();
        try {
            invoiceManager.deleteInvoice(id_invoice);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    /* COMMENTS */


    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public ResponseEntity getComments(){

        // Cambiar esto por el usuario que se quiera
        List<Comment> comments = commentManager.getAllComments();

        if(!comments.isEmpty()){
            return new ResponseEntity(commentManager.commentsToJSON(comments), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public ResponseEntity insertComment(@RequestBody Comment comment) {

        try {
            System.out.println("INSERTANDO COMENTARIO");
            User user = null;

            System.out.println(comment.getUser().stringToJSON());
            if(comment.getUser() != null){

                 user = userManager.getUserByEmail(comment.getUser().getEmail());
                 System.out.println("Usuario");
            }

            if (user != null) {
                comment.setUser(user);
            }

            commentManager.saveCommentInDataBase(comment);

            return new ResponseEntity(HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody User user) {
        try {
            userManager.saveUser(user);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }


}
