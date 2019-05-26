package com.esliceu.keep_it_safe.entities;


import com.esliceu.keep_it_safe.RolUser;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUser rol_user;

    private String name;
    private String first_surname;
    private String second_surname;

    @OneToMany(mappedBy = "user")
    private Set<Invoice> invoices;

    public long getUser_id() { return user_id; }

    public void setUser_id(long user_id) { this.user_id = user_id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUser getRol_user() { return rol_user; }

    public void setRol_user(RolUser rol_user) { this.rol_user = rol_user; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_surname() {
        return first_surname;
    }

    public void setFirst_surname(String first_surname) {
        this.first_surname = first_surname;
    }

    public String getSecond_surname() {
        return second_surname;
    }

    public void setSecond_surname(String second_surname) {
        this.second_surname = second_surname;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return this.getEmail() + " " + this.getName() + " " + this.getFirst_surname() + " " + this.getSecond_surname() + " " + this.getRol_user() ;
    }

}
