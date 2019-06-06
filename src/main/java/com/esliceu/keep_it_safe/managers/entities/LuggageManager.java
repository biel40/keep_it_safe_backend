package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.repository.LuggageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LuggageManager {
    private LuggageRepository luggageRepository;

    @Autowired
    public  LuggageManager(LuggageRepository luggageRepository) {
        this.luggageRepository = luggageRepository;
    }
}
