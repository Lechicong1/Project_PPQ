package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class CourseRequest {
    @NotBlank(message = "Tên khóa học không được để trống")
    private String nameCourse;
    private String description;
    @NotNull(message = "Phí khóa học không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Phí khóa học phải lớn hơn hoặc bằng 0")
    @Digits(integer = 10, fraction = 2, message = "Phí khóa học chỉ được có tối đa 10 chữ số nguyên và 2 chữ số thập phân")
    private BigDecimal fee;
    @NotNull(message = "Số buổi học không được để trống")
    private Integer numberSessions;
    @NotBlank(message = "Ngôn ngữ không được để trống")
    private String language;


}
