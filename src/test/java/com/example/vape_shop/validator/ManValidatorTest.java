package com.example.vape_shop.validator;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.services.ManService;
import org.apache.tomcat.jni.Error;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ManValidatorTest {

    @Autowired
    private ManValidator manValidator;
    @MockBean
    private ManService manService;


    @Test
    void validateTest() {
        Errors errors = Mockito.mock(Errors.class);
        Man man = Mockito.mock(Man.class);
        man.setUserEmail("test@mail.ru");
        Mockito.when(manService.getManByEmail(Mockito.anyString())).thenReturn(man);

        Assertions.assertNotNull(manService.getManByEmail("test@mail.ru"));
        manValidator.validate(man, errors);
        Assertions.assertNotNull(errors);
        Mockito.verify(manService, Mockito.times(1)).getManByEmail("test@mail.ru");

    }
}