package com.esliceu.keep_it_safe.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "rol")
    private RolUser rol_user;

    private String name;
    private String surnames;
    private String imageUrl;

    @OneToMany(mappedBy = "user")
    private Set<Invoice> invoices;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments;

    public long getUser_id() { return userId; }

    public void setUser_id(long user_id) { this.userId = user_id; }

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

    public String getSurnames() { return surnames; }

    public void setSurnames(String surnames) { this.surnames = surnames; }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }




    public String stringToJSON() {
        return  "{\"userId\":" + this.userId + "," +
                "\"email\":\""+this.email+"\", " +
                "\"name\":\""+this.name+"\", "+
                "\"surnames\":\""+this.surnames+"\"," +
                "\"rol_user\":\""+this.rol_user+"\", " +
                "\"imageUrl\":\""+this.imageUrl+"\"}";
    }


}
