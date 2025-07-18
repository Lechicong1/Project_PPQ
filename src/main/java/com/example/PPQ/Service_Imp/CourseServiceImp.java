package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.CourseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseServiceImp {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseByID(int id);
    void addCourse(CourseRequest courseRequest, MultipartFile file);
    void updateCourse(int id, CourseRequest courseRequest,MultipartFile file );
    void deleteCourse(int id);
    List<String> getAllLanguages();
    List<CourseDTO> getAllCoursesByLanguage(String language);
    List<CourseDTO> getCourseByIdStudent();
}
