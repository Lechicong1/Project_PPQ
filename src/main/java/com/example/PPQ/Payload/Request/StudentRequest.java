package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.*;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Float getScore1() {
        return score1;
    }

    public void setScore1(Float score1) {
        this.score1 = score1;
    }

    public Float getScore2() {
        return score2;
    }

    public void setScore2(Float score2) {
        this.score2 = score2;
    }

    public Float getScore3() {
        return score3;
    }

    public void setScore3(Float score3) {
        this.score3 = score3;
    }

    public Float getScoreHomework() {
        return scoreHomework;
    }

    public void setScoreHomework(Float scoreHomework) {
        this.scoreHomework = scoreHomework;
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
