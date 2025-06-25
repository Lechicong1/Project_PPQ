package com.example.PPQ.Payload.Response;

import java.math.BigDecimal;

public class QRRespone {
    private String qrUrl;        // Link ảnh QR
    private String qrContent;    // Nội dung chuyển khoản (VD: PPQ123)
    private BigDecimal amount;       // Số tiền phải chuyển
    private String bankCode;     // Mã ngân hàng (VD: VCB)
    private String bankName;     // Tên ngân hàng đầy đủ (VD: Vietcombank)
    private String accountNumber;// STK nhận tiền
    private String accountName;  // Tên người nhận

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
