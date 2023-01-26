package com.example.vape_shop.validator;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.services.ManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ManValidator implements Validator {

    private final ManService manService;

    @Autowired
    public ManValidator(ManService manService) {
        this.manService = manService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Man.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Man man = (Man) target;
        if (manService.getManByEmail(man.getUserEmail()) != null) {
            errors.rejectValue("userEmail", "", "Этот email уже занят");
        }
    }
}
