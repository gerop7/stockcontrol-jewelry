package com.gerop.stockcontrol.jewelry.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy=UniqueSkuValidation.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSku {
    String message() default "Sku not avaible";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
