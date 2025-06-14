package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.RegisterCourseRequest;
import com.example.PPQ.Payload.Response.CourseRegisterRespone;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.RegisterCourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course/register")
public class RegisterCourseController {
    @Autowired
    RegisterCourseService registerCourseService;
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(value = "/{idcourse}")
    public ResponseEntity<?> registerCourse(@PathVariable int idcourse,@Valid @RequestBody RegisterCourseRequest registerCourseRequest)
    {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(registerCourseService.RegisterCourse(idcourse, registerCourseRequest)){

            responseData.setMessage("Successfully registered");
            responseData.setSuccess(Boolean.TRUE);
        }
        else{
            responseData.setMessage("Failed to register");
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping
    public ResponseEntity<?> getAllCourseRegister(){
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<CourseRegisterRespone> courseRegisterRespones = registerCourseService.getAllCourseRegister();
        if(courseRegisterRespones.size() > 0){
            responseData.setSuccess(Boolean.TRUE);
            responseData.setData(courseRegisterRespones);

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("Chưa có học sinh nào đăng kí khóa học ");
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping("/search")
    public ResponseEntity<?> getCourseRegisterBySearch(@RequestParam(required = false) Integer idcourse, @RequestParam(required = false) String nameStudent,@RequestParam(required = false) Integer idClass){
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<CourseRegisterRespone> courseRegisterRespones = registerCourseService.searchCourseRegister(idcourse,nameStudent,idClass);
        if(courseRegisterRespones.size() > 0){
            responseData.setSuccess(Boolean.TRUE);
            responseData.setData(courseRegisterRespones);

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("Chưa có học sinh nào đăng kí khóa học này ");
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }

}
