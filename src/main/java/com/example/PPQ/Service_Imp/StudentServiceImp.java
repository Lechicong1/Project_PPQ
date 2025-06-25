package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.Student_response;
import com.example.PPQ.Service.StudentService;
import com.example.PPQ.respository.StudentRespository;

import java.util.List;

public interface StudentServiceImp {
    List<Student_response> getAllStudents();
    Student_response getStudentById(int id);
//    boolean addStudent(StudentRequest student);
    boolean updateStudent(int id,StudentRequest student);
    boolean deleteStudent(int id);
    List<Student_response> getAllStudentByClass(int class_id);
    Student_response myInfo();
    List<Student_response> searchBynameAndPhoneNumber(String name,String phoneNumber);
}
