package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.otpRequest;
import com.example.PPQ.ServiceImp.UserService;
import com.example.PPQ.ServiceImp.mail.EmailService;
import com.example.PPQ.ServiceImp.mail.otpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/otp")
public class otpController {
    @Autowired
    otpService otpservice;
    @Autowired
    EmailService emailservice;
    @Autowired
    UserService userservice;
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody otpRequest otpRequest) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpservice.saveOtp(otpRequest.getEmail(), otp, 300); // TTL: 300 giây = 5 phút
        emailservice.sendOtpEmail(otpRequest.getEmail(), otp);
        return ResponseEntity.ok("OTP đã được gửi đến email: " + otpRequest.getEmail());
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody otpRequest otpRequest) {
        boolean isValid = otpservice.checkOtp(otpRequest.getEmail(), otpRequest.getOtp());
        if (isValid) {

            return ResponseEntity.ok("Xác thực thành công!");
        } else {
            return ResponseEntity.badRequest().body("OTP không đúng hoặc đã hết hạn.");
        }
    }


}
