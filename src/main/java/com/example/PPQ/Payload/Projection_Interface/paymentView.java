package com.example.PPQ.Payload.Projection_Interface;

import java.math.BigDecimal;

public interface paymentView {
    Integer getId();
    String getFullName();
    String getPhoneNumber();
    String getQrContent();
    String getStatus();
    BigDecimal getAmount();
    String getClassName();
    String getNameCourse();
    Integer getCourseId();
    Integer getUserId();
    Integer getClassId();
}
