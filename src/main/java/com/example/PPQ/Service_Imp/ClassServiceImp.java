package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.Class_response;
import com.example.PPQ.Payload.Response.Course_response;

import java.util.List;

public interface ClassServiceImp {
    List<Class_response> getAllClasses();
    List<Class_response> getClassByIdTeacher();
    boolean addClasses(ClassRequest classRequest);
    boolean updateClass(int id, ClassRequest classRequest);
    boolean deleteClass(int id);
    List<Class_response> getClassByCourse(int idCourse);
}
