package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.Constants;
import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Stock;
import com.esliceu.keep_it_safe.exception.NoStockException;
import com.esliceu.keep_it_safe.exception.StockOverflowException;
import com.esliceu.keep_it_safe.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean checkStock(Invoice invoice) throws NoStockException {

        Calendar start = Calendar.getInstance();

        start.setTime(invoice.getStart_date().getTime());

        List<Calendar> dates = new ArrayList<>();
        dates.add(start);


        long dateDiff = this.getDayDiff(invoice);

        for (int i = 0; i <= dateDiff; i++) {

            Stock stock = stockRepository.findByDay(start);

            if (stock == null) {
                this.createStock(start.getTime(), invoice);
            } else {
                int currentStock = stock.getCurrentStock();
                int stockInINvoice = invoice.getLuggages().size();
                if (currentStock >= stockInINvoice) {
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

    @Transactional
    public void restoreStock(Invoice invoice) throws StockOverflowException {

        Calendar start = Calendar.getInstance();

        start.setTime(invoice.getStart_date().getTime());

        long dateDiff = this.getDayDiff(invoice);
        System.out.println("Dia de diferencia "+ dateDiff);
        for (int i = 0; i <= dateDiff; i++) {
            System.out.println("Ha entrado");
            Stock stock = stockRepository.findByDay(start);
            int currentStock = stock.getCurrentStock();
            int stockInINvoice = invoice.getLuggages().size();

            int stockRestore =  currentStock + stockInINvoice;
            if (stockRestore > stock.getStock()) {
                throw new StockOverflowException(stock.getDay().getTime());
            }
            stock.setCurrentStock(currentStock + stockInINvoice);
            stockRepository.save(stock);

            this.addDayOneDay(start);
        }

    }


    private long getDayDiff(Invoice invoice) {

        return ChronoUnit.DAYS.between(invoice.getStart_date().toInstant(), invoice.getEnd_date().toInstant());

    }

    private void createStock(Date day, Invoice invoice) {

        Stock stock = this.context.getBean(Stock.class);
        stock.setCurrentStock(Constants.STOCK - invoice.getLuggages().size());
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

    public void recalculateStock(Invoice invoice) {

    }

}
