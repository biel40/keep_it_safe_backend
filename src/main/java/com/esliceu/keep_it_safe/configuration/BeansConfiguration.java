package com.esliceu.keep_it_safe.configuration;

import com.esliceu.keep_it_safe.entities.Comment;
import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Luggage;
import com.esliceu.keep_it_safe.entities.User;
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
    public Gson gson(){ return new Gson();}
}
