package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.otpRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.mailServiceImp.otpService;
import com.example.PPQ.ServiceImp.UserServiceImp;
import com.example.PPQ.ServiceImp.mail.EmailServiceImp;
import com.example.PPQ.ServiceImp.mail.otpServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/otp")
public class otpController {
    @Autowired
    otpService otpservice;
    @Autowired
    EmailServiceImp emailservice;
    @Autowired
    UserServiceImp userservice;
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody otpRequest otpRequest) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpservice.saveOtp(otpRequest.getEmail(), otp, 300); // TTL: 300 giây = 5 phút
        emailservice.sendOtpEmail(otpRequest.getEmail(), otp);
        ResponseData responseData = new ResponseData();
        responseData.setMessage("OTP đã được gửi đến email của bạn");
        responseData.setSuccess(true);
        return ResponseEntity.status(200).body(responseData);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody otpRequest otpRequest) {
        boolean isValid = otpservice.checkOtp(otpRequest.getEmail(), otpRequest.getOtp());
        ResponseData responseData = new ResponseData();

        if (isValid) {
            responseData.setMessage("Xác thực thành công , mật khẩu mới sẽ được gửi vào email của bạn");
            responseData.setSuccess(true);
        } else {
            responseData.setMessage("OTP không đúng hoặc đã hết hạn");
            responseData.setSuccess(false);
        }
        HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(responseData);
    }


}
