package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.Class_response;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")

public class ClassController {
    @Autowired
    ClassService classService;
    @GetMapping
    public ResponseEntity<?> getAllClasses() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<Class_response> classResponses =classService.getAllClasses();
        if(classResponses.size()>0) {
            responseData.setData(classResponses);
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
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/teachers")
    public ResponseEntity<?> getClassById() {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<Class_response> class_dto = classService.getClassByIdTeacher();
        if(class_dto !=null) {
            responseData.setData(class_dto);
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
    @PostMapping
    public ResponseEntity<?> addClasses( @Valid @RequestBody ClassRequest classRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;

        if(classService.addClasses(classRequest)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;
            responseData.setMessage("Thêm lớp học thành công ");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Thêm lớp học thất bại");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateClass( @Valid @PathVariable int id,@RequestBody ClassRequest classRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(classService.updateClass(id,classRequest)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Cập nhật lớp học thành công");

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Cập nhật lớp học thất bại");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(classService.deleteClass(id)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Xóa lớp học thành công");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Xóa lớp học thất bại");

        }
        return ResponseEntity.status(status).body(responseData);
    }
}
