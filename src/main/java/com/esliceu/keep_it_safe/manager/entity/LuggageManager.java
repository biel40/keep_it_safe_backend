package com.esliceu.keep_it_safe.manager.entity;

import com.esliceu.keep_it_safe.entity.Luggage;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LuggageManager {

    private LuggageRepository luggageRepository;

    @Autowired
    public  LuggageManager(LuggageRepository luggageRepository) {
        this.luggageRepository = luggageRepository;
    }

    public void saveAllLuggages(List<Luggage> luggages) {
        luggageRepository.saveAll(luggages);
    }

    public List<Luggage> findAllLuggages() {
        return (List<Luggage>) luggageRepository.findAll();
    }

}
