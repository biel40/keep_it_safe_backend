package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Luggage;
import org.springframework.data.repository.CrudRepository;

public interface LuggageRepository extends CrudRepository<Luggage, Integer> {
}
