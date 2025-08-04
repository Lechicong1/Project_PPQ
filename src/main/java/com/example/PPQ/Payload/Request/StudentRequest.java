package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {
    @NotBlank(message = "Tên học sinh không được để trống")
    private String fullName;
//    @NotNull(message = "User không được để trống")
//    private Integer idUsers;
    @Pattern(
            regexp = "^(03|05|07|08|09)\\d{8}$",
            message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 03, 05, 07, 08 hoặc 09 và đủ 10 chữ số."
    )
    private String phoneNumber;
//    private int idStudent;
    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }
    @Min(0)
    @Max(10)
    private Float score1;
    @Min(0)
    @Max(10)
    private Float score2;
    @Min(0)
    @Max(10)
    private Float score3;
    @Min(0)
    @Max(10)
    private Float scoreHomework;


}
