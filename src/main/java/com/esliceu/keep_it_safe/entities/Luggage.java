package com.esliceu.keep_it_safe.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Luggage {
    @Id
    private String luggage_type;

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
}
