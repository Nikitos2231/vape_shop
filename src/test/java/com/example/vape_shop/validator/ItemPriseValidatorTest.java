package com.example.vape_shop.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ItemPriseValidatorTest {

    @InjectMocks
    private ItemPriseValidator itemPriseValidator;

    @Test
    void isValidTest() {
        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Assertions.assertTrue(itemPriseValidator.isValid(123, constraintValidatorContext));
        Assertions.assertTrue(itemPriseValidator.isValid(0, constraintValidatorContext));
        Assertions.assertFalse(itemPriseValidator.isValid(-12, constraintValidatorContext));
    }
}