package com.example.vape_shop.custom_annotation;

import com.example.vape_shop.validator.ItemPriseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ItemPriseValidator.class)
@Documented
public @interface IsDigital {

    String message() default "{itemPrise.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
