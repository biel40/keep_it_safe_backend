package com.esliceu.keep_it_safe.entities;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
public class Invoice {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoice_id;

    @Column(nullable = false)
    private double total_price;

    @Column(nullable = false)

    private Instant start_date;

    @Column(nullable = false)

    private Instant end_date;

    @Column(nullable = false)
    private boolean isVerified;


    @OneToMany(mappedBy = "invoice")
    private Set<Luggage> luggages;


    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public long getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(long invoice_id) {
        this.invoice_id = invoice_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public Instant getStart_date() {
        return start_date;
    }

    public void setStart_date(Instant start_date) {
        this.start_date = start_date;
    }

    public Instant getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Instant end_date) {
        this.end_date = end_date;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Set<Luggage> getLuggages() {
        return luggages;
    }

    public void setLuggages(Set<Luggage> luggages) {
        this.luggages = luggages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
