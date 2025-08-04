package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassRequest {
    @NotBlank(message = "Tên lớp không được để trống")
    private String className;
    @NotNull(message = "Khóa học không được để trống")
    private Integer idCourses;
    @NotNull(message = "Giáo viên không được để trống")
    private Integer idTeachers;
    @NotNull(message = "Số lượng học sinh không được để trống")
    @Max(value=20,message = "Lớp chỉ đối đa 20 học viên")
    @Positive(message = "Số lượng học sinh phải lớn hơn 0")
    private Integer maxStudents;
    private String status;
    private String roadMap;


}
