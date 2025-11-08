package com.gerop.stockcontrol.jewelry.exception.jewel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JewelPermissionDeniedException extends RuntimeException{

    public JewelPermissionDeniedException() {
    }

    public JewelPermissionDeniedException(String message) {
        super(message);
    }
}
