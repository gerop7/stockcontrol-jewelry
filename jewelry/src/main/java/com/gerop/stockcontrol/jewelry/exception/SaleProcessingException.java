package com.gerop.stockcontrol.jewelry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaleProcessingException extends RuntimeException {

    private final String errorCode = "SALE_PROCESSING_ERROR";

    public SaleProcessingException(String message) {
        super(message);
    }

    public SaleProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
