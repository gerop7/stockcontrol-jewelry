package com.gerop.stockcontrol.jewelry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(Long categoryId, String type) {
        super("La "+type+" con ID: "+categoryId+", no fue encontrada.");
    }

    public CategoryNotFoundException(String categoryName, String type) {
        super("La "+type+" con ID: "+categoryName+", no fue encontrada.");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
