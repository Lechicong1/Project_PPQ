package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.ClassDTO;

import java.util.List;

public interface ClassService {
    List<ClassDTO> getAllClasses();
    List<ClassDTO> getClassByIdTeacher();
    void addClasses(ClassRequest classRequest);
    void updateClass(int id, ClassRequest classRequest);
    void deleteClass(int id);
    List<ClassDTO> getClassByCourse(int idCourse);
}
