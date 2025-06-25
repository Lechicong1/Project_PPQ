package com.example.PPQ.Controller;

import com.example.PPQ.Entity.CourseEntity;
import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.Course_response;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    CourseService courseService;
    @GetMapping("/languages")
    public ResponseEntity<?> getLanguages() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<String> languages =courseService.getAllLanguages();
        if(languages.size()>0) {
            responseData.setData(languages);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData(null);
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/myCourse")
    public ResponseEntity<?> getCourseByIdStudent() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<Course_response> course_dto =courseService.getCourseByIdStudent();
        if(course_dto.size()>0) {
            responseData.setData(course_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData(null);
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping
    public ResponseEntity<?> getAllCourses(@RequestParam(required = false) String languages) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<Course_response> course_dto =new ArrayList<>();
        if(languages!=null) {
            course_dto=courseService.getAllCoursesByLanguage(languages);
        }
        else{
            course_dto=courseService.getAllCourses();
        }
        if(course_dto.size()>0) {
            responseData.setData(course_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData(null);
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping(value="/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable int courseId) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        Course_response course_dto = courseService.getCourseByID(courseId);
        if(course_dto !=null) {
            responseData.setData(course_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData(null);
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("Không tìm thấy khóa học");
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addCourse( @Valid  @RequestPart("courseRequestStr") String courseRequestStr, @RequestPart("file") MultipartFile file) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        ObjectMapper objectMapper = new ObjectMapper();
        CourseRequest courseRequest = null;
        try {
            courseRequest = objectMapper.readValue(courseRequestStr, CourseRequest.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid teacherRequest JSON");
        }
        if(courseService.addCourse(courseRequest,file)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;
            responseData.setMessage("course added successfully");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("course not added successfully");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable int id, @Valid  @RequestPart("courseRequestStr") String courseRequestStr, @RequestPart(value = "file", required = false)  MultipartFile file) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        ObjectMapper objectMapper = new ObjectMapper();
        CourseRequest courseRequest = null;
        try {
            courseRequest = objectMapper.readValue(courseRequestStr, CourseRequest.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid teacherRequest JSON");
        }
        if(courseService.updateCourse(id,courseRequest,file)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Course updated successfully");

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Course not updated successfully");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(courseService.deleteCourse(id)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Course deleted successfully");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Course not deleted successfully");

        }
        return ResponseEntity.status(status).body(responseData);
    }
}
