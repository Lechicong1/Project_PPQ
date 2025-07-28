package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    public String getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(String roadMap) {
        this.roadMap = roadMap;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getIdCourses() {
        return idCourses;
    }

    public void setIdCourses(Integer idCourses) {
        this.idCourses = idCourses;
    }

    public Integer getIdTeachers() {
        return idTeachers;
    }

    public void setIdTeachers(Integer idTeachers) {
        this.idTeachers = idTeachers;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
