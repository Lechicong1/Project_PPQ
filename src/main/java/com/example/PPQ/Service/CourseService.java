package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.CourseDTO;
import com.example.PPQ.Payload.Response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    PageResponse<CourseDTO> getAllCourses(String language, Integer page, Integer size , String sortOption);
    CourseDTO getCourseByID(int id);
    void addCourse(CourseRequest courseRequest, MultipartFile file);
    void updateCourse(int id, CourseRequest courseRequest,MultipartFile file );
    void deleteCourse(int id);
    List<String> getAllLanguages();

    List<CourseDTO> getCourseByIdStudent();
}
