package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.ClassDTO;
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
        List<ClassDTO> classResponses =classService.getAllClasses();
        responseData.setData(classResponses);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/{idCourse}")
    public ResponseEntity<?> getClassByIdCourse(@PathVariable int idCourse) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<ClassDTO> classResponses =classService.getClassByCourse(idCourse);
            responseData.setData(classResponses);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/teachers")
    public ResponseEntity<?> getClassById() {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<ClassDTO> class_dto = classService.getClassByIdTeacher();
            responseData.setData(class_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PostMapping
    public ResponseEntity<?> addClasses( @Valid @RequestBody ClassRequest classRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;

       classService.addClasses(classRequest);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;
            responseData.setMessage("Thêm lớp học thành công ");
        return ResponseEntity.status(status).body(responseData);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateClass( @Valid @PathVariable int id,@RequestBody ClassRequest classRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        classService.updateClass(id,classRequest);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Cập nhật lớp học thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        classService.deleteClass(id);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Xóa lớp học thành công");
        return ResponseEntity.status(status).body(responseData);
    }
}
