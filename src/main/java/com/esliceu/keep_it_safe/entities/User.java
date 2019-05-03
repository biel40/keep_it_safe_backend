package com.esliceu.keep_it_safe.entities;


import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    private String DNI_NIE;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private String first_surname;
    private String second_surname;

    @OneToMany(mappedBy = "user")
    private Set<Invoice> invoices;

}
