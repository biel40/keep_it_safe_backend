package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.Constants;
import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Stock;
import com.esliceu.keep_it_safe.exception.NoStockException;
import com.esliceu.keep_it_safe.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class StockManager {

    private StockRepository stockRepository;
    private ApplicationContext context;

    @Autowired
    public StockManager(StockRepository stockRepository, ApplicationContext context) {
        this.stockRepository = stockRepository;
        this.context = context;
    }

    @Transactional
    public boolean checkStock(Invoice invoice) throws RuntimeException{

        Calendar start = invoice.getStart_date();
        Calendar end = invoice.getEnd_date();

        List<Calendar> dates = new ArrayList<>();
        dates.add(start);


        long dateDiff = ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
        System.out.println("DIA DE DIFERENCIAS: " + dateDiff);

        for (int i = 0; i <= dateDiff; i++) {

            Stock stock = stockRepository.findByDay(start);


            if( stock == null ) {
                this.createStock(start.getTime(), invoice);
            } else {
                int currentStock  = stock.getCurrentStock();
                int stockInINvoice =  invoice.getLuggages().size();
                if(currentStock >= stockInINvoice) {
                    stock.setCurrentStock(currentStock - stockInINvoice);
                    stockRepository.save(stock);
                } else {
                    throw new NoStockException(stock.getDay().getTime());
                }
            }

            this.addDayOneDay(start);
        }

        return true;
    }

    private void createStock(Date day, Invoice invoice) {

        Stock stock = this.context.getBean(Stock.class);
        stock.setCurrentStock(Constants.STOCK -  invoice.getLuggages().size());
        stock.setStock(Constants.STOCK);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        stock.setDay(calendar);

        stockRepository.save(stock);

    }

    private Calendar addDayOneDay(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        return calendar;
    }
}
