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

    public String getDNI_NIE() {
        return DNI_NIE;
    }

    public void setDNI_NIE(String DNI_NIE) {
        this.DNI_NIE = DNI_NIE;
    }

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


}
