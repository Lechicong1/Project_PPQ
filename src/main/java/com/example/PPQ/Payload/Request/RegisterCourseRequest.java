package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCourseRequest {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    @Pattern(
            regexp = "^(03|05|07|08|09)\\d{8}$",
            message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 03, 05, 07, 08 hoặc 09 và đủ 10 chữ số."
    )
    @NotBlank(message = "số điện thoại không được để trống")
    private String phoneNumber;
    @NotNull(message = "Lớp học không được để trống")
    private int idClass;

}
