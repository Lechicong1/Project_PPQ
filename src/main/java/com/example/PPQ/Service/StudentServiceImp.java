package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.StudentDTO;

import java.util.List;

public interface StudentServiceImp {
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(int id);
//    boolean addStudent(StudentRequest student);
    void updateStudent(int id,StudentRequest student);
    void deleteStudent(int id);
    List<StudentDTO> getAllStudentByClass(int class_id);
    StudentDTO myInfo();
    List<StudentDTO> searchBynameAndPhoneNumber(String name, String phoneNumber);
}
