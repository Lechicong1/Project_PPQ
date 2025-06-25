package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.Course_response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseServiceImp {
    List<Course_response> getAllCourses();
    Course_response getCourseByID(int id);
    boolean addCourse(CourseRequest courseRequest, MultipartFile file);
    boolean updateCourse(int id, CourseRequest courseRequest,MultipartFile file );
    boolean deleteCourse(int id);
    List<String> getAllLanguages();
    List<Course_response> getAllCoursesByLanguage(String language);
    List<Course_response> getCourseByIdStudent();
}
