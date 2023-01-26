package com.example.vape_shop.services;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.ManRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private ManRepository manRepositories;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MailSender mailSender;

    @Test
    void register() throws MessagingException {
        Man man = new Man();
        man.setUserEmail("test@mail.ru");
        man.setUserPassword("123");
        Mockito.doReturn("fwergreg").when(passwordEncoder).encode(Mockito.anyString());
        Mockito.doReturn(false).when(mailSender).send("test@mail.ru", "Activation code", "greg");

        boolean expected = registrationService.register(man);

        Assertions.assertTrue(expected);
        Assertions.assertEquals("ROLE_GUEST", man.getUserRole());
        Assertions.assertNotEquals("123", man.getUserPassword());
        Assertions.assertNotNull(man.getActivationCode());
        Mockito.verify(manRepositories).save(man);

    }

    @Test
    void activateUser_NotNullTest() {
        Man man = new Man();
        Mockito.doReturn(man).when(manRepositories).findByActivationCode("123");

        boolean expected = registrationService.activateUser("123");

        Assertions.assertTrue(expected);
        Assertions.assertNull(man.getActivationCode());
        Assertions.assertEquals("ROLE_USER", man.getUserRole());
        Mockito.verify(manRepositories).save(man);
    }

    @Test
    void activateUser_NullTest() {
        Mockito.doReturn(null).when(manRepositories).findByActivationCode("123");

        boolean expected = registrationService.activateUser("123");

        Assertions.assertFalse(expected);
    }
}