package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class TeacherRequest {
    @NotBlank(message = "Tên giáo viên không được để trống")
    private String fullName;
    @Pattern(
            regexp = "^(03|05|07|08|09)\\d{8}$",
            message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 03, 05, 07, 08 hoặc 09 và đủ 10 chữ số."
    )
    private String phoneNumber;
    @NotBlank(message = "trình độ giáo dục không được để trống ")
    private String educationLevel;
    private String description;
    @NotNull(message = "Trường user không được để trống")
    private Integer idUsers;
    @NotBlank(message = "email không được để trống ")
    private String email;

}
