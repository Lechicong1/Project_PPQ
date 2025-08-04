package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.RegisterCourseRequest;
import com.example.PPQ.Payload.Response.CourseRegisterRespone;

import java.util.List;

public interface RegisterCourseService {

    List<CourseRegisterRespone> getAllCourseRegister();
    List<CourseRegisterRespone> searchCourseRegister(Integer nameCourse, String nameStudent, Integer nameClass);
}
