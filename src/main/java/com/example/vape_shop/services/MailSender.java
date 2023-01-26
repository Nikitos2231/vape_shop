package com.example.vape_shop.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailSender {

    private static final Logger logger = LogManager.getLogger(MailSender.class);
    private final JavaMailSender javaMailSender;
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    private Map<String, Object> context1 = new HashMap<>();

    @Autowired
    public MailSender(JavaMailSender javaMailSender, JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String username;

    public boolean send(String emailTo, String subject, String message) throws MessagingException {
        MimeMessage message1 = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message1,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context1.put("emailTo", emailTo);
        context1.put("link", message);
        context.setVariables(context1);
        String emailContent = templateEngine.process("mail.html", context);

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(emailTo);
        mimeMessageHelper.setText(emailContent, true);

        try {
            javaMailSender.send(message1);
        } catch (MailException e) {
            logger.warn("Не удалось отправить сообщение для подтверждения аккаунта на почту: {}", emailTo);
            return true;
        }
        return false;
    }
}
