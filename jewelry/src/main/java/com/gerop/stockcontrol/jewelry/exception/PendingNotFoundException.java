package com.gerop.stockcontrol.jewelry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PendingNotFoundException extends RuntimeException{

    public PendingNotFoundException() {
    }

    public PendingNotFoundException(String message) {
        super(message);
    }

}
