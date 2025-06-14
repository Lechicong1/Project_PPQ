package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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

//    public int getIdStudent() {
//        return idStudent;
//    }
//
//    public void setIdStudent(int idStudent) {
//        this.idStudent = idStudent;
//    }
//
//    public Integer getIdUsers() {
//        return idUsers;
//    }
//
//    public void setIdUsers(Integer idUsers) {
//        this.idUsers = idUsers;
//    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
