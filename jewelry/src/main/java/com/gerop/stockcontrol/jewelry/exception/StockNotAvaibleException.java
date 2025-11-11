package com.gerop.stockcontrol.jewelry.exception;

public class StockNotAvaibleException extends RuntimeException{

    public StockNotAvaibleException() {
    }

    public StockNotAvaibleException(String message) {
        super(message);
    }

}
