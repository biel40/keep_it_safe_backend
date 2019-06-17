package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.entities.Stock;
import com.esliceu.keep_it_safe.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Component
public class StockManager {

    private StockRepository stockRepository;

    @Autowired
    public StockManager(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public boolean checkStock(Calendar start, Calendar end) {
        List<Calendar> dates = new ArrayList<>();
        dates.add(start);

        while (start.equals(end)){
            start = this.addDay(start, 1);
            dates.add(start);
        }
        System.out.println(dates.toString());
     return true;
    }

    private Calendar addDay(Calendar cal, int days) {
        cal.add(Calendar.DATE, days);
        return cal;
    }
}
