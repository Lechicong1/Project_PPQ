package com.example.PPQ.ServiceImp.mail;

import com.example.PPQ.ServiceImp.UserService;
import com.example.PPQ.Service.mailServiceImp.otpServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
@Service
public class otpService implements otpServiceImp {
    private final RedisTemplate<String, String> redisTemplate;
    @Autowired
    public otpService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Autowired
    UserService userservice;
    @Autowired
    EmailService emailservice;
    @Override
    public void saveOtp(String email, String otp, long time) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, otp, Duration.ofSeconds(time));
    }

    @Override
    public void deleteOtp(String email) {
        redisTemplate.delete(email);
    }

    @Override
    public boolean checkOtp(String email, String inputOtp) {
        String otp = redisTemplate.opsForValue().get(email);
        if(otp != null && otp.equals(inputOtp)){
            redisTemplate.delete(email);
            userservice.setDefaultPassword(email);
            // gửi lại mail kèm mật khẩu
            emailservice.sendnewPassword(email);
            return true;
        }
    return false;
    }
}
