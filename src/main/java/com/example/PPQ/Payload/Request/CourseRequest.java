package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CourseRequest {
    @NotBlank(message = "Tên khóa học không được để trống")
    private String nameCourse;
    private String description;
    @NotNull(message = "Phí khóa học không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Phí khóa học phải lớn hơn hoặc bằng 0")
    @Digits(integer = 10, fraction = 2, message = "Phí khóa học chỉ được có tối đa 10 chữ số nguyên và 2 chữ số thập phân")
    private BigDecimal fee;

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
