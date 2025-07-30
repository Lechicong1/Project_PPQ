package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.PaymentRequest;
import com.example.PPQ.Payload.Response.PaymentRespone;
import com.example.PPQ.Payload.Response.QRRespone;

import java.util.List;

public interface PaymentServiceImp {
    Integer createPayment(PaymentRequest paymentRequest,int idCourse);
    QRRespone generateQr(int Paymentid);
    void confirmPayment(PaymentRequest paymentRequest,int paymentId);
    List<PaymentRespone> getAllPayments();
}
