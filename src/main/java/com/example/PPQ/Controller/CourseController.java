package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Response.CourseDTO;
import com.example.PPQ.Payload.Response.PageResponse;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.CourseService;
import com.example.PPQ.ServiceImp.CourseServiceImp;
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
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping("/languages")
    public ResponseEntity<?> getLanguages() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<String> languages = courseService.getAllLanguages();
        responseData.setData(languages);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/myCourse")
    public ResponseEntity<?> getCourseByIdStudent() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<CourseDTO> course_dto = courseService.getCourseByIdStudent();
        responseData.setData(course_dto);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping
    public ResponseEntity<?> getAllCourses(@RequestParam(required = false) String languages,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) String sort) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        PageResponse<CourseDTO> pageCourse =courseService.getAllCourses(languages, (page==null)? 0 : page - 1, size==null?5: size,sort);
        responseData.setData(pageCourse);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable int courseId) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        CourseDTO course_dto = courseService.getCourseByID(courseId);
        responseData.setData(course_dto);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addCourse(@Valid @RequestPart("courseRequestStr") String courseRequestStr, @RequestPart("file") MultipartFile file) {
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
        courseService.addCourse(courseRequest, file);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.CREATED;
        responseData.setMessage("Thêm khóa học thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable int id, @Valid @RequestPart("courseRequestStr") String courseRequestStr, @RequestPart(value = "file", required = false) MultipartFile file) {
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
        courseService.updateCourse(id, courseRequest, file);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        responseData.setMessage("Cập nhật khóa học thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        courseService.deleteCourse(id);
        {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Xóa khóa học thành công");
            return ResponseEntity.status(status).body(responseData);
        }
    }
}
