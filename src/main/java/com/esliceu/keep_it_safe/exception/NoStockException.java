package com.esliceu.keep_it_safe.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoStockException extends RuntimeException{
    private Date day;

    public NoStockException(Date day) {
        this.day =  day;
    }

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String dateString = format.format( this.day);


        return "No hay suficiente stock en la fecha " + dateString;
    }
}
