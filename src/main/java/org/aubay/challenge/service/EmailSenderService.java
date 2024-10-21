package org.aubay.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    @Autowired
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String fromEmailId;

    @Async("asyncTaskExecutor")
    public void sendEmail(String toEmailId, String subject, String body, String logMessage){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmailId);
        mailMessage.setTo(toEmailId);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        sender.send(mailMessage);

        logger.info(logMessage);
    }

}
