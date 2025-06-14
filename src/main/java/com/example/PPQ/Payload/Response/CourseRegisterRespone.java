package com.example.PPQ.Payload.Response;

import java.time.LocalDateTime;

public class CourseRegisterRespone {
    private int idCourse;
    private int idStudent;
    private int idClass;
    private String nameCourse;
    private String nameStudent;
    private String nameClass;
    private LocalDateTime EnrollmentDate;

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public int getIdClass() {
        return idClass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public LocalDateTime getEnrollmentDate() {
        return EnrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        EnrollmentDate = enrollmentDate;
    }
}
