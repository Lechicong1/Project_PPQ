package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.Student_response;
import com.example.PPQ.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
//@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {
    @Autowired
    StudentService studentService;
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<Student_response> student_dto=studentService.getAllStudents();
        if(student_dto.size()>0) {
            responseData.setData(student_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("All students successfully retrieved");

        }
        else{
            responseData.setData(null);
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/class")
    //tim hoc sinh theo lop
    public ResponseEntity<?> getStudentByClass(@RequestParam int classId) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<Student_response> student_dto = studentService.getAllStudentByClass(classId);
        if(student_dto!=null) {
            responseData.setData(student_dto);
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
    @GetMapping("/search")
    public ResponseEntity<?> getStudentByName(@RequestParam(required = false ) String name,@RequestParam(required = false) String phoneNumber) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<Student_response> student_dto = studentService.searchBynameAndPhoneNumber(name,phoneNumber);
        if(student_dto!=null) {
            responseData.setData(student_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Không tìm thấy sinh viên !");
        }
        return ResponseEntity.status(status).body(responseData);
    }

//    @PostMapping
//    public ResponseEntity<?> addStudent( @Valid @RequestBody StudentRequest studentRequest) {
//        ResponseData responseData = new ResponseData();
//        HttpStatus status = HttpStatus.OK;
//        if(studentService.addStudent(studentRequest)) {
//            responseData.setSuccess(Boolean.TRUE);
//            status = HttpStatus.CREATED;
//            responseData.setMessage("Thêm học sinh thành công");
//        }
//        else{
//            responseData.setSuccess(Boolean.FALSE);
//            status = HttpStatus.NOT_FOUND;
//            responseData.setMessage("Thêm học sinh thất bại");
//        }
//        return ResponseEntity.status(status).body(responseData);
//    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @Valid @RequestBody StudentRequest studentRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(studentService.updateStudent(id,studentRequest)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Student updated successfully");

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Student not updated successfully");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(studentService.deleteStudent(id)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Student deleted successfully");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Student not deleted successfully");

        }
        return ResponseEntity.status(status).body(responseData);
    }

}
