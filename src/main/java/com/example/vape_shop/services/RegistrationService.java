package com.example.vape_shop.services;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.ManRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.util.UUID;

@Service
public class RegistrationService {

    private static final Logger logger = LogManager.getLogger(RegistrationService.class);

    private final ManRepository manRepositories;
    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    @Autowired
    public RegistrationService(ManRepository manRepositories, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.manRepositories = manRepositories;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Transactional
    public boolean register(Man man) throws MessagingException {
        man.setUserRole("ROLE_GUEST");

        String encodedPassword = passwordEncoder.encode(man.getUserPassword());
        man.setUserPassword(encodedPassword);
        man.setActivationCode(UUID.randomUUID().toString());
        if (!StringUtils.isEmpty(man.getUserEmail())) {
            String message = String.format("http://localhost:8080/activate/%s", man.getActivationCode());
            if (mailSender.send(man.getUserEmail(), "Activation code", message)) {
                logger.info("Человек попытался зарегистрироваться, но указал несуществующую почту");
                return false;
            }
        }
        logger.info("Человек зарегестрировался, но не подтвердил свою почту");
        manRepositories.save(man);
        return true;
    }

    public boolean activateUser(String code) {
        Man man = manRepositories.findByActivationCode(code);

        if (man == null) {
            return false;
        }
        man.setActivationCode(null);
        man.setUserRole("ROLE_USER");
        manRepositories.save(man);
        logger.info("Человек с email: {} Успешно подтвердил свою почту", man.getUserEmail());
        return true;
    }
}