package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.TeacherDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface  TeacherServiceImp {
     public void addTeacher(TeacherRequest teacherRequest, MultipartFile file);
    public  void deleteTeacher(int id);
     public void updateTeacher(int id,TeacherRequest teacherRequest,MultipartFile file);
    public  List<TeacherDTO> getAllTeacher();
//    public List<Teacher_response> getTeacherByName(String name);
    public TeacherDTO myInfo();
}
