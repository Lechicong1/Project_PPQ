package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.contactRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.mailServiceImp.EmailService;
import com.example.PPQ.ServiceImp.mail.EmailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
public class mailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody contactRequest request) {
            ResponseData responseData =new ResponseData();
            HttpStatus status = HttpStatus.OK;
            if( emailService.sendContactEmail(request)){
                responseData.setMessage("Gửi tin nhắn thành công !Chúng tôi sẽ liên hệ lại với bạn trong thời gian sớm nhất ");
                status = HttpStatus.OK;
                responseData.setSuccess(Boolean.TRUE);
            }

            else{
                responseData.setSuccess(Boolean.FALSE);
                responseData.setMessage("Gửi tin nhắn thất bại ");
            }

      return ResponseEntity.status(status).body(responseData);
    }
}
