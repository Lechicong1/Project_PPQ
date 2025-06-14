package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class registerRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;
    @Size(min = 6, max = 20, message = "Mật khẩu phải từ 6 đến 20 ký tự")
    @Pattern(
            regexp = "^[A-Z][a-z0-9@#$%^&+=]*$",
            message = "Mật khẩu phải bắt đầu bằng chữ in hoa, theo sau là chữ thường/số/ký tự đặc biệt"
    )
    private String password;
    @Size(min = 6, max = 20, message = "Mật khẩu phải từ 6 đến 20 ký tự")
    @Pattern(
            regexp = "^[A-Z][a-z0-9@#$%^&+=]*$",
            message = "Mật khẩu phải bắt đầu bằng chữ in hoa, theo sau là chữ thường/số/ký tự đặc biệt"
    )
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
