package com.gerop.stockcontrol.jewelry.exception.jewel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JewelNotFoundException extends RuntimeException {

    public JewelNotFoundException(String sku) {
        super("No se encontró la joya con SKU: " + sku);
    }

    public JewelNotFoundException(Long jewelId) {
        super("No se encontró la joya con ID: " + jewelId);
    }

    public JewelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
