package com.esliceu.keep_it_safe.entities;

import com.esliceu.keep_it_safe.LuggageType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Luggage {
    @Id
    @Enumerated(EnumType.STRING)
    private LuggageType luggage_type;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double high_dimension;

    @Column(nullable = false)
    private double width_dimension;

    @Column(nullable = false)
    private double deep_dimension;

    @ManyToMany(mappedBy = "luggages")
    private Set<Invoice> invoices;

    public LuggageType getLuggage_type() {
        return luggage_type;
    }

    public void setLuggage_type(LuggageType luggage_type) {
        this.luggage_type = luggage_type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHigh_dimension() {
        return high_dimension;
    }

    public void setHigh_dimension(double high_dimension) {
        this.high_dimension = high_dimension;
    }

    public double getWidth_dimension() {
        return width_dimension;
    }

    public void setWidth_dimension(double width_dimension) {
        this.width_dimension = width_dimension;
    }

    public double getDeep_dimension() {
        return deep_dimension;
    }

    public void setDeep_dimension(double deep_dimension) {
        this.deep_dimension = deep_dimension;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
}
