package com.example.vape_shop.validator;

import com.example.vape_shop.custom_annotation.DateOfBirth;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, Date> {

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate dateStart = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(dateStart, LocalDate.now());
        return period.getYears() >= 18;
    }
}