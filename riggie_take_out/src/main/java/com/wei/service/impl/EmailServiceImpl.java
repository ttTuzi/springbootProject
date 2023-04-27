package com.wei.service.impl;


import com.wei.entity.EmailDetails;
import com.wei.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailer;

    @Value("${spring.mail.username}")
    private String sender;

    public boolean sendMail(EmailDetails details) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(sender);
            msg.setTo(details.getRecipient());
            msg.setText(details.getBody());
            msg.setSubject(details.getSubject());
            mailer.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
