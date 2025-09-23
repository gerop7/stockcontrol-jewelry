package com.gerop.stockcontrol.jewelry.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.gerop.stockcontrol.jewelry.repository.JewelRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueNameValidation implements ConstraintValidator<UniqueName, String> {

    @Autowired
    private JewelRepository repository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return (repository.existByName(name));
    }

}
