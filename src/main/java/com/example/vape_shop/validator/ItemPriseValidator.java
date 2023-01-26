package com.example.vape_shop.validator;

import com.example.vape_shop.custom_annotation.IsDigital;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ItemPriseValidator implements ConstraintValidator<IsDigital, Integer> {

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches("\\d+", String.valueOf(integer.intValue()));
    }
}
