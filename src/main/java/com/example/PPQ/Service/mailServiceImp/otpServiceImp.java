package com.example.PPQ.Service.mailServiceImp;

public interface otpServiceImp {



    void saveOtp(String email,String otp,long time);
    void deleteOtp(String email);
    boolean checkOtp(String email,String inputotp);
}
