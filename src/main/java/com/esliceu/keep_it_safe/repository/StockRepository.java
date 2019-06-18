package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;

public interface StockRepository  extends CrudRepository<Stock, Integer> {
    Stock findByDay(Calendar calendar);
}
