package com.example.vape_shop.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;
import java.util.Date;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DateOfBirthValidatorTest {

    @InjectMocks
    private DateOfBirthValidator dateOfBirthValidator;

    @Test
    void isValidTest() {
        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);

        Assertions.assertFalse(dateOfBirthValidator.isValid(new Date(), constraintValidatorContext));
        Assertions.assertTrue(dateOfBirthValidator.isValid(new Date(60 * 60 * 24 * 365 * 19), constraintValidatorContext));
        Assertions.assertTrue(dateOfBirthValidator.isValid(new Date(60 * 60 * 24 * 365 * 18), constraintValidatorContext));
    }
}