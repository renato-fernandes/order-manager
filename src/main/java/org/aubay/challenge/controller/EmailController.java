package org.aubay.challenge.controller;

import org.aubay.challenge.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/sendEmail")
    public String sendEmail(){

        String toEmail = "renato.fernandes.lisboa@gmail.com";
        String subject = "Order Manager - Notification";
        String body = "This email is a notification from Order Manager API.";
        String logMessage = "[EMAIL SENT] test email sent to " + toEmail;

        emailSenderService.sendEmail(toEmail, subject, body, logMessage);

        return "email sent";
    }

}
