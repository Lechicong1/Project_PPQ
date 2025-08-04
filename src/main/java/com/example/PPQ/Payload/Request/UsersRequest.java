package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersRequest {
    private int ID;
    @NotBlank(message = "UserName không được để trống")
    private String userName;
    @Size(min=6,max=20,message = "Mật khẩu phải từ 6 đến 20 kí tự ")
    @Pattern(
            regexp = "^[A-Z][a-z0-9@#$%^&+=]*$",
            message = "Mật khẩu phải bắt đầu bằng chữ in hoa, theo sau là chữ thường/số/ký tự đặc biệt"
    )
    private String passWord;
    @NotNull(message = "Role không được để trống")
    private Integer idRoles;
    
}
