package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

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
//    @NotNull(message = "Ngày bắt đầu làm việc không được để trống")
//    @PastOrPresent(message = "Ngày bắt đầu làm việc phải là ngày hiện tại hoặc trong quá khứ")
//    private LocalDate startDate;
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(Integer idUsers) {
        this.idUsers = idUsers;
    }
}
