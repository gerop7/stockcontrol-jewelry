package com.gerop.stockcontrol.jewelry.exception.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InventoryAccessDeniedException extends RuntimeException {

    private final String errorCode = "INVENTORY_ACCESS_DENIED";

    public InventoryAccessDeniedException(String message) {
        super(message);
    }

    public InventoryAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }
}