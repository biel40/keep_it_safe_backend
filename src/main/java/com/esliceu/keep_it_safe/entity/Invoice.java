package com.esliceu.keep_it_safe.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
public class Invoice {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoice_id;

    @Column(nullable = false)
    private double total_price;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)

    private Calendar start_date;


    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar end_date;

    @Column(nullable = false)
    private boolean verified;

    @ManyToMany
    @JoinTable(name = "invoice_luggage",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "luggage_id"))
    private List<Luggage> luggages;


    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public Calendar getStart_date() {
        return start_date;
    }

    public void setStart_date(Calendar start_date) {
        this.start_date = start_date;
    }

    public Calendar getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Calendar end_date) {
        this.end_date = end_date;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<Luggage> getLuggages() {
        return luggages;
    }

    public void setLuggages(List<Luggage> luggages) {
        this.luggages = luggages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String stringToJSON(){
        return  "{\"invoice_id\": " + this.invoice_id + ", \"total_price\": " + this.total_price+"," +
                " \"end_date\": \"" + this.end_date.toInstant()+"\", " +
                "\"start_date\":" + " \"" + this.start_date.toInstant()+"\", " +
                "\"user\":" + this.user.stringToJSON() + "," +
                "\"verified\":" + this.verified + "," +
                "\"luggages\":" + this.getJsonFormatLuggages() + "}";
    }

    private String getJsonFormatLuggages(){

        StringBuilder luggagesToJSON = new StringBuilder();

        luggagesToJSON.append("[");

        for (Luggage luggage : this.luggages) {
            luggagesToJSON.append(luggage.stringToJSON());
            luggagesToJSON.append(",");
        }

        luggagesToJSON.deleteCharAt(luggagesToJSON.length()-1);
        luggagesToJSON.append("]");

        return luggagesToJSON.toString();
    }
}
