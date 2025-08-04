package com.example.PPQ.Payload.Response;

import com.example.PPQ.Payload.Projection_Interface.CourseRegisterView;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CourseRegisterRespone {
    private int idCourse;
    private int idStudent;
    private int idClass;
    private String nameCourse;
    private String nameStudent;
    private String nameClass;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollmentDate;

    public CourseRegisterRespone(){}
    public CourseRegisterRespone(CourseRegisterView c){
        this.nameCourse = c.getNameCourse();
        this.nameClass = c.getNameClass();
        this.nameStudent=c.getFullName();
        this.enrollmentDate=c.getEnrollmentDate();
    }

}
