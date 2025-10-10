package com.gerop.stockcontrol.jewelry.exception;

public class StockNotAvailableException extends RuntimeException{

    public StockNotAvailableException(){
        super();
    }

    public StockNotAvailableException(String message) {
        super(message);
    }

    public StockNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
