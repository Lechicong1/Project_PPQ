package com.example.PPQ.Payload.Response;

import com.example.PPQ.Payload.Projection_Interface.paymentView;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class PaymentRespone {
    private Integer classId;
    private String fullName;
    private String phoneNumber;
    private Integer userId;
    private Integer courseId;
    private String nameCourse;
    private String nameClass;
    private String status;
    private BigDecimal amount;
    private Integer paymentId;
    private String qrContent;

    public PaymentRespone() {
    }
    public PaymentRespone(paymentView res){
        this.fullName = res.getFullName();
        this.phoneNumber = res.getPhoneNumber();
        this.paymentId= res.getId();
        this.nameCourse = res.getNameCourse();
        this.nameClass= res.getClassName();
        this.status= res.getStatus();
        this.amount = res.getAmount();
        this.qrContent = res.getQrContent();
        this.courseId = res.getCourseId();
        this.classId = res.getClassId();
        this.userId = res.getUserId();
    }

}
