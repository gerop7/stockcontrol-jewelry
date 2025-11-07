package com.gerop.stockcontrol.jewelry.exception.material;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MaterialNotFoundException extends RuntimeException {

    public MaterialNotFoundException(Long materialId,String type) {
        super("No se encontró un material "+type+" con ID: " + materialId);
    }

    public MaterialNotFoundException(String message) {
        super(message);
    }
}
