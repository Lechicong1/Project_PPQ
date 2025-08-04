package com.example.PPQ.Payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class changePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;


}
