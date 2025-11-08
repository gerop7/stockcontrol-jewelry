package com.gerop.stockcontrol.jewelry.exception.material;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MaterialPermissionDeniedException extends RuntimeException{
    private final String errorCode = "MATERIAL_PERMISSION_DENIED";

    public MaterialPermissionDeniedException() {
    }

    public MaterialPermissionDeniedException(String message) {
        super(message);
    }

    public MaterialPermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
