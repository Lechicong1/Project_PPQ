package com.example.PPQ.Service;

import com.example.PPQ.Exception.EmailException;
import com.example.PPQ.Payload.Request.contactRequest;
import com.example.PPQ.Service_Imp.EmailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServiceImp {

    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Override
    public boolean sendContactEmail(contactRequest contact) {

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo("Phuonglee.cv@gmail.com");
            mailMessage.setSubject("Liên hệ từ người dùng: Tư vấn khóa học");
            mailMessage.setText(
                    "Họ tên: " + contact.getName() + "\n" +

                            "SĐT: " + contact.getPhone() + "\n" +
                            "Nội dung:\n" + contact.getMessage()
            );
            // Sending the mail
            mailSender.send(mailMessage);
            return true;
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
           throw new EmailException("Lỗi khi gửi tới email admin" + e.getMessage());
        }
    }
}
