package com.gerop.stockcontrol.jewelry.exception.material;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaterialMismatchException extends RuntimeException {

    public MaterialMismatchException(Long jewelId, Long materialId, String type) {
        super("El " + type + " con ID " + materialId + " no pertenece a la joya " + jewelId + ".");
    }

    public MaterialMismatchException(String jewelSku, Long materialId, String type) {
        super("El " + type + " con ID " + materialId + " no pertenece a la joya " + jewelSku + ".");
    }

    public MaterialMismatchException(String message) {
        super(message);
    }
}

