package com.esliceu.keep_it_safe.entities;

import org.springframework.data.annotation.Id;

public class LoginUser {

    @Id
    private String id;

    private String userName;
    private String password;
    private String token;

    public LoginUser(String username, String password){
        this.userName = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
