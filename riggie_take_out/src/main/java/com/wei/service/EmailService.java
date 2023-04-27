package com.wei.service;


import com.wei.entity.EmailDetails;

public interface EmailService {
    boolean sendMail(EmailDetails details);
}
