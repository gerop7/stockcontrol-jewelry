package com.gerop.stockcontrol.jewelry.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.gerop.stockcontrol.jewelry.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidation implements ConstraintValidator<UniqueUsername, String>{

    @Autowired
    private UserRepository repository;
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return (!repository.existByUsername(username));
    }

}
