package com.example.PPQ.Util;

import java.math.BigDecimal;

public class VietQRUtil {
    public static String generateVietQRUrl(
            String bankCode,
            String accountNumber,
            String accountName,
            BigDecimal amount,
            String content
    ) {
        return String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.jpg?amount=%.0f&addInfo=%s&accountName=%s",
                bankCode,
                accountNumber,
                amount,
                content,
                accountName.replace(" ", "+")
        );
    }
}

