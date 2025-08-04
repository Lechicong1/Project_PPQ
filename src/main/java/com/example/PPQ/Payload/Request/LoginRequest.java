package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "username không được để trống")
    private String userName;
    @NotBlank(message = "password không được để trống")
    private String password;


}
