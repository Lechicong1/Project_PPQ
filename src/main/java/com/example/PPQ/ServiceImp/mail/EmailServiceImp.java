package com.example.PPQ.ServiceImp.mail;

import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Exception.EmailException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.contactRequest;
import com.example.PPQ.Service.mailServiceImp.EmailService;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService {
    @Autowired
    UsersRepository userRepo;
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

    @Override
    public void sendOtpEmail(String toEmail,String otp) {
        UserEntity user = userRepo.findByUsername(toEmail);
        if(user == null) {
            throw new ResourceNotFoundException("Email này chưa đăng kí tài khoản");
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message=
               "Mã OTP của bạn là: " + otp +
              "  OTP sẽ hết hạn sau 5 phút.";

        // Setting up necessary details
        mailMessage.setFrom(sender);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Mã xác thực OTP");
        mailMessage.setText(message);
        // Sending the mail
        mailSender.send(mailMessage);
    }

    @Override
    public void sendnewPassword(String toEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message= "Mật khẩu mới của bạn là:" + "Lechicong" ;

        // Setting up necessary details
        mailMessage.setFrom(sender);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Mật khẩu mới");
        mailMessage.setText(message);
        // Sending the mail
        mailSender.send(mailMessage);
    }


}
