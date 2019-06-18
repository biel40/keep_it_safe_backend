package com.esliceu.keep_it_safe.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StockOverflowException extends RuntimeException{
    private Date day;

    public StockOverflowException(Date day) {
        this.day =  day;
    }

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String dateString = format.format( this.day);


        return "Erro al restaurar stock, demasiado stock para añadir en el día " + dateString;
    }

}
