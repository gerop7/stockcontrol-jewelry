package com.gerop.stockcontrol.jewelry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(Long stockId) {
        super("No se encontró el stock con ID: " + stockId);
    }

    public StockNotFoundException(String message) {
        super(message);
    }
}
