package com.gerop.stockcontrol.jewelry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(Number quantity) {
        super("Cantidad inválida: " + quantity);
    }

    public InvalidQuantityException(String message) {
        super(message);
    }

    public InvalidQuantityException(String message, Throwable cause) {
        super(message, cause);
    }
}
