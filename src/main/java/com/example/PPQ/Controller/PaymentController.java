package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.PaymentRequest;
import com.example.PPQ.Payload.Response.PaymentRespone;
import com.example.PPQ.Payload.Response.QRRespone;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.ServiceImp.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value="/{idCourse}")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest, @PathVariable  int idCourse) {
        System.out.println(idCourse);
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        Integer idPayment = paymentService.createPayment(paymentRequest, idCourse);
        System.out.println(idPayment);
        if (idPayment !=null) {
            responseData.setSuccess(Boolean.TRUE);
            responseData.setData(idPayment);
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("Lỗi khi tạo thanh toán");
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value="/{idPayment}")
    public ResponseEntity<?> generateQr(@PathVariable int idPayment) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        QRRespone paymentRespone = paymentService.generateQr(idPayment);
        if (paymentRespone != null) {
            responseData.setSuccess(Boolean.TRUE);
            responseData.setData(paymentRespone);

        }

        else{
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("Lỗi khi tạo QR");
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value="/{idPayment}")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequest paymentRequest, @PathVariable  int idPayment) {

        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        paymentService.confirmPayment(paymentRequest, idPayment);
        responseData.setSuccess(Boolean.TRUE);
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<PaymentRespone> listpaymentRespone = paymentService.getAllPayments();
        System.out.println(listpaymentRespone);
        if (listpaymentRespone != null) {
            responseData.setSuccess(Boolean.TRUE);
            responseData.setData(listpaymentRespone);

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("Có lỗi khi tải danh sách thanh toán ");
        }
        return ResponseEntity.status(status).body(responseData);
    }
}
