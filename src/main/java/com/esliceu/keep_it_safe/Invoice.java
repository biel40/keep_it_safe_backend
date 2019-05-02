package com.esliceu.keep_it_safe;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id_Invoice;

    @Column(nullable = false)
    private double total_price;

    @Column(nullable = false)
    private String start_date;

    @Column(nullable = false)
    private String end_date;

    @Column(nullable = false)
    private boolean isVerified;

    @ManyToOne
    private User user;

    @ManyToMany
    private Set<Luggage> luggages;
}
