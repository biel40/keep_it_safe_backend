package com.esliceu.keep_it_safe.configuration;

import com.esliceu.keep_it_safe.entity.*;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeansConfiguration {

    @Bean
    @Scope("prototype")
    public User user(){ return new User();}

    @Bean
    @Scope("prototype")
    public Luggage luggage(){ return new Luggage();}

    @Bean
    @Scope("prototype")
    public Invoice invoice(){ return new Invoice();}

    @Bean
    @Scope("prototype")
    public Comment comment(){return new Comment();}

    @Bean
    @Scope("prototype")
    public Stock stock(){return new Stock();}


    @Bean
    public Gson gson(){ return new Gson();}
}
