package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.entities.LoginUser;
import com.esliceu.keep_it_safe.repository.LoginUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginUserController {

    @Autowired
    private LoginUserRepository loginUserRepository;

    @RequestMapping("/createDatabaseUsers")
    public void createUsers(){

        loginUserRepository.deleteAll();

        loginUserRepository.save(new LoginUser("biel", "biel"));
        loginUserRepository.save(new LoginUser("javi" , "javi"));

        System.out.println(loginUserRepository.findAll().toString());

    }
}
