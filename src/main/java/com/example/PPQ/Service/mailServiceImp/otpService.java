package com.example.PPQ.Service.mailServiceImp;

public interface otpService {



    void saveOtp(String email,String otp,long time);
    void deleteOtp(String email);
    boolean checkOtp(String email,String inputotp);
}
