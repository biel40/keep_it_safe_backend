package com.esliceu.keep_it_safe.entities;

import javax.persistence.*;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date start_date;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date end_date;

    @Column(nullable = false)
    private boolean isVerified;

    @ManyToMany
    @JoinTable(name = "invoice_luggage",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "luggage_id"))
    private Set<Luggage> luggages;


    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
