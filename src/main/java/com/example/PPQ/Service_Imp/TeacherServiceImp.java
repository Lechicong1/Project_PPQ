package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.Teacher_response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherServiceImp {
     public boolean addTeacher(TeacherRequest teacherRequest, MultipartFile file);
    public  boolean deleteTeacher(int id);
     public boolean updateTeacher(int id,TeacherRequest teacherRequest,MultipartFile file);
    public  List<Teacher_response> getAllTeacher();
    public List<Teacher_response> getTeacherByName(String name);
    public Teacher_response myInfo();
}
