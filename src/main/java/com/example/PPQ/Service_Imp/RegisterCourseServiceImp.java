package com.example.PPQ.Service_Imp;

import com.example.PPQ.Entity.CourseStudentClassEntity;
import com.example.PPQ.Payload.Request.RegisterCourseRequest;
import com.example.PPQ.Payload.Response.CourseRegisterRespone;
import com.example.PPQ.Service.RegisterCourseService;

import java.util.List;

public interface RegisterCourseServiceImp {
    boolean RegisterCourse(int id_course , RegisterCourseRequest RegisterCourseRequest);
    List<CourseRegisterRespone> getAllCourseRegister();
    List<CourseRegisterRespone> searchCourseRegister(Integer nameCourse, String nameStudent, Integer nameClass);
}
