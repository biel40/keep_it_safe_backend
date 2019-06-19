package com.esliceu.keep_it_safe.controller;

import com.esliceu.keep_it_safe.entity.Comment;
import com.esliceu.keep_it_safe.entity.Invoice;
import com.esliceu.keep_it_safe.entity.Luggage;
import com.esliceu.keep_it_safe.entity.User;
import com.esliceu.keep_it_safe.exception.NoStockException;
import com.esliceu.keep_it_safe.exception.StockOverflowException;
import com.esliceu.keep_it_safe.manager.entity.CommentManager;
import com.esliceu.keep_it_safe.manager.entity.InvoiceManager;
import com.esliceu.keep_it_safe.manager.entity.LuggageManager;
import com.esliceu.keep_it_safe.manager.entity.UserManager;
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
    private final LuggageManager luggageManager;
    private final UserManager userManager;
    private final CommentManager commentManager;

    @Autowired
    public MainController(LuggageManager luggageManager, UserManager userManager, InvoiceManager invoiceManager, CommentManager commentManager) {
        this.luggageManager = luggageManager;
        this.userManager = userManager;
        this.invoiceManager = invoiceManager;
        this.commentManager = commentManager;
    }

    /* LUGGAGES */

    @RequestMapping(value = "/luggages", method = RequestMethod.GET)
    public List<Luggage> getLuggages() {
        return (List<Luggage>) luggageManager.findAllLuggages();
    }

    @RequestMapping(value = "/luggages", method = RequestMethod.POST)
    public void insertLuggages(@RequestBody Luggage luggage) {

    }

    @RequestMapping(value = "/luggages/price", method = RequestMethod.PUT)
    public ResponseEntity changePrices(@RequestBody List<Luggage> luggages){
        try {
            luggageManager.saveAllLuggages(luggages);
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
            if (invoice.getEnd_date().after(now) ||  invoice.getEnd_date().get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    invoice.getEnd_date().get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) && invoice.getEnd_date().get(Calendar.MONTH) ==  now.get(Calendar.MONTH)){
                currentInvoices.add(invoice);
            }
        }
        return invoiceManager.invoicesToJSON(currentInvoices);
    }

    @RequestMapping(value = "/invoices/edit", method = RequestMethod.PUT)
    public ResponseEntity updateInvoice(@RequestBody Invoice invoice) {
        try {

            Invoice invoiceToEdit = invoiceManager.getInvoiceById((int) invoice.getInvoice_id());

            invoiceToEdit.setInvoice_id((int) invoice.getInvoice_id());
            invoiceToEdit.setVerified(invoice.isVerified());
            invoiceToEdit.setStart_date(invoice.getStart_date());
            invoiceToEdit.setEnd_date(invoice.getEnd_date());
            invoiceToEdit.setTotal_price(invoice.getTotal_price());
            invoiceToEdit.setLuggages(invoice.getLuggages());


            invoiceManager.editInvoice(invoice);

            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }


    @RequestMapping(value = "/invoice/user/notVerified/{userID}", method = RequestMethod.GET)
    public String getClientReservations(@PathVariable long userID) {

        User user = userManager.getUserByID(userID);

        List<Invoice> allInvoicesFromClient = this.invoiceManager.getInvoicesByUser(user);
        List<Invoice> filteredInvoices = new LinkedList<>();

        for(Invoice invoice : allInvoicesFromClient) {
            if(!invoice.isVerified()) {
                filteredInvoices.add(invoice);
            }
        }

        return invoiceManager.invoicesToJSON(filteredInvoices);

    }


    @RequestMapping(value = "/invoice/id/{invoiceId}", method = RequestMethod.GET)
    public String getInvoiceToCheck(@PathVariable int invoiceId) {
        return  invoiceManager.getInvoiceById(invoiceId).stringToJSON();
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public ResponseEntity saveInvoice(@RequestBody Invoice invoice) {

        User user = userManager.getUserByEmail(invoice.getUser().getEmail());
        if (user == null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        try {
            invoiceManager.saveInvoice(invoice);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NoStockException e) {
            return new ResponseEntity<>(e.toString(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/invoice/{idString}", method = RequestMethod.DELETE)
    public ResponseEntity deleteInvoice(@PathVariable String idString){

        int idInvoice = Integer.parseInt(idString.replace("=", ""));

        try {
            invoiceManager.deleteInvoice(idInvoice);
            return new ResponseEntity(HttpStatus.OK);
        } catch (StockOverflowException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(),HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/invoice/validate/", method = RequestMethod.PUT)
    public ResponseEntity validateInvoice(@RequestBody Invoice invoice){

        try {

            Invoice invoiceToEdit = invoiceManager.getInvoiceById(invoice.getInvoice_id());
            invoiceToEdit.setVerified(true);
            invoiceManager.saveInvoice(invoiceToEdit);

            return new ResponseEntity(HttpStatus.OK);
        } catch (StockOverflowException e){

            return new ResponseEntity<>(e.toString(),HttpStatus.CONFLICT);
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
            User user = null;

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

    /* USERS */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody User user) {
        try {
            userManager.saveUser(user);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }


    @RequestMapping(value = "/user/profile", method = RequestMethod.PUT)
    public ResponseEntity editUser(@RequestBody User user) {
        try {
            String[] tokens = userManager.updateUser(user);
            return new ResponseEntity<>(tokens,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/user/password", method = RequestMethod.PUT)
    public ResponseEntity editPassword(@RequestBody String[] data) {
        try {
            String[] tokens = userManager.updatePassword(data);
            if (tokens !=  null) {
                return new ResponseEntity<>(tokens,HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/user/email", method = RequestMethod.GET)
    public User getUserByEmail(@RequestParam("email") String email) {
        System.out.println(email);
        return this.userManager.getUserByEmail(email);
    }


}
