package com.esliceu.keep_it_safe.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
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
    @JsonIgnore
    public Set<Invoice> getInvoice() {
        return invoices;
    }

    public void setInvoice(Set<Invoice> invoices) {
        this.invoices = invoices;
    }


    @Override
    public String toString() {

        return "{\"luggage_type\": \"" + this.luggage_type + "\", \"deep_dimension\": " + this.deep_dimension + ", " +
                "\"high_dimension\":" + this.high_dimension + ", \"width_dimension\":" + this.width_dimension + ", " +
                "\"price\":" + this.price + "}";

    }
}
