package com.gerop.stockcontrol.jewelry.exception.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(Long inventoryId) {
        super("No existe el inventario con ID: " + inventoryId);
    }

    public InventoryNotFoundException(String message) {
        super(message);
    }
}
