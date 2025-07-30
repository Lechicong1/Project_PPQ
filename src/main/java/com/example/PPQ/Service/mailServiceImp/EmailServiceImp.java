package com.example.PPQ.Service.mailServiceImp;

import com.example.PPQ.Payload.Request.contactRequest;

public interface EmailServiceImp {
    boolean sendContactEmail(contactRequest contact);
    void sendOtpEmail(String toEmail,String otp);
    void sendnewPassword(String toEmail);
}
