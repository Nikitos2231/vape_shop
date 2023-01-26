package com.example.vape_shop.controllers;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.services.RegistrationService;
import com.example.vape_shop.validator.ManValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;


@Controller
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private final RegistrationService registrationService;
    private final ManValidator manValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, ManValidator manValidator) {
        this.registrationService = registrationService;
        this.manValidator = manValidator;
    }

    @GetMapping("/auth/login")
    public String getAuthPage() {
        return "auth/login";
    }

    @GetMapping("/auth/registration")
    public String registrationPage(@ModelAttribute("man") Man man) {
        return "auth/registration";
    }

    @PostMapping("/auth/registration")
    public String performRegistration(@ModelAttribute("man") @Valid Man man, BindingResult bindingResult, Model model) throws MessagingException {
        manValidator.validate(man, bindingResult);
        man.setAvatar("https://e7.pngegg.com/pngimages/831/88/png-clipart-user-profile-computer-icons-user-interface-mystique-miscellaneous-user-interface-design.png");
        if (bindingResult.hasErrors()) {
            logger.info("Человек ввел невалидные данные при регистрации");
            return "auth/registration";
        }
        if (!registrationService.register(man)) {
            model.addAttribute("message", "Извините, вы неправильно написали свою почту!");
            logger.info("Человек ввел невалидную почту при регистрации");
            return "auth/login";
        }
        model.addAttribute("message", "На электронную почту было отправленно сообщение для подтверждения аккаунта!");
        return "auth/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable("code") String code) {
        boolean isActivated = registrationService.activateUser(code);
        if(isActivated) {
            model.addAttribute("messageCode", "Вы успешно подтвердили свою электронную почту!");
        }
        else  {
            model.addAttribute("messageCode", "Код активации не найден!");
        }
        return "auth/login";
    }
}
