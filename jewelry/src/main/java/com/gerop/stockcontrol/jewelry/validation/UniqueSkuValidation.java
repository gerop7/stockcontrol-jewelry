package com.gerop.stockcontrol.jewelry.validation;

import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueSkuValidation implements ConstraintValidator<UniqueSku, String> {

    private final JewelRepository repository;
    private final UserServiceHelper helper;

    public UniqueSkuValidation(JewelRepository repository, UserServiceHelper helper) {
        this.repository = repository;
        this.helper = helper;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return repository.existsBySkuAndUserId(value, helper.getCurrentUser().getId());
    }

}
