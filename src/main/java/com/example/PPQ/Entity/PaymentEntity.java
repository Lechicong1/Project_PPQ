package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.PaymentRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // hoặc AUTO
    private Integer id;
    private Integer userId;
    private Integer courseId;
    private Integer classId;
    private String fullName;
    private String phoneNumber;
    private BigDecimal amount;
    private String status;  // pending / success / failed
    private String qrContent; // Nội dung chuyển khoản, ví dụ: PPQ123
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    public PaymentEntity() {}

}
