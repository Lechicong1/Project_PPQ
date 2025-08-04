package com.example.PPQ.Payload.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class PaymentRequest {
    private Integer classId;
    private String fullName;
    private String phoneNumber;
    private Integer userId;
    private Integer courseId;



}
