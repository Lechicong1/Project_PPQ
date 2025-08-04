package com.example.PPQ.Payload.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class QRRespone {
    private String qrUrl;        // Link ảnh QR
    private String qrContent;    // Nội dung chuyển khoản (VD: PPQ123)
    private BigDecimal amount;       // Số tiền phải chuyển
    private String bankCode;     // Mã ngân hàng (VD: VCB)
    private String bankName;     // Tên ngân hàng đầy đủ (VD: Vietcombank)
    private String accountNumber;// STK nhận tiền
    private String accountName;  // Tên người nhận


}
