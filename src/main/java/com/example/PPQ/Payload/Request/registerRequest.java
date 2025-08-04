package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class registerRequest {
    @NotBlank(message = "Username không được để trống")
    @Email(message = "Email không hợp lệ")
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


}
