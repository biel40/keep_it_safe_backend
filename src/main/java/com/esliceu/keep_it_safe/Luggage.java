package com.esliceu.keep_it_safe;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Luggage")
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

    @ManyToMany
    private Set<Invoice> invoices;
}
