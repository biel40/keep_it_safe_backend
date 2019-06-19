package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entity.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;

public interface StockRepository  extends CrudRepository<Stock, Integer> {
    Stock findByDay(Calendar calendar);
}
