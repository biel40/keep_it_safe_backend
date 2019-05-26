package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.entities.Luggage;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    private final LuggageRepository luggageRepository;
    private final UserRepository userRepository;

    public MainController(LuggageRepository luggageRepository, UserRepository userRepository) {
        this.luggageRepository = luggageRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/luggage", method = RequestMethod.GET)
    public List<Luggage> getLuggages() {
        return (List<Luggage>) this.luggageRepository.findAll();
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity foo(@RequestBody User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return  new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

}
