package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.registerRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody registerRequest registerRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
            if(registerService.register(registerRequest)){
                status= HttpStatus.OK;
                responseData.setMessage("dang ki thanh cong");
                responseData.setSuccess(Boolean.TRUE);

            }
            else{
                status= HttpStatus.BAD_REQUEST;
                responseData.setMessage("dang ki that bai");
                responseData.setSuccess(Boolean.FALSE);

            }
        return ResponseEntity.status(status).body(responseData);

    }

}
