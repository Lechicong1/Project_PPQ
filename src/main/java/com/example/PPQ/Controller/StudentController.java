package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.PageResponse;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.StudentDTO;
import com.example.PPQ.Service.StudentService;
import com.example.PPQ.ServiceImp.StudentServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {
    @Autowired
    StudentService studentService;
    @GetMapping
    public ResponseEntity<?> getAllStudents( @RequestParam(required = false) String name ,
                                             @RequestParam(required = false) String phone ,
                                             @RequestParam Integer page,
                                             @RequestParam Integer size) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        PageResponse<StudentDTO> pageStudentReponse=studentService.getAllStudents(name, phone,page-1, size);
            responseData.setData(pageStudentReponse);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/class")
    //tim hoc sinh theo lop
    public ResponseEntity<?> getStudentByClass(@RequestParam int classId) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<StudentDTO> student_dto = studentService.getAllStudentByClass(classId);
            responseData.setData(student_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
//    @GetMapping("/search")
//    public ResponseEntity<?> getStudentByName(@RequestParam(required = false ) String name,@RequestParam(required = false) String phoneNumber) {
//        ResponseData responseData = new ResponseData();
//        HttpStatus status ;
//        List<StudentDTO> student_dto = studentService.searchBynameAndPhoneNumber(name,phoneNumber);
//            responseData.setData(student_dto);
//            responseData.setSuccess(Boolean.TRUE);
//            status = HttpStatus.OK;
//
//        return ResponseEntity.status(status).body(responseData);
//    }

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
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @Valid @RequestBody StudentRequest studentRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        studentService.updateStudent(id,studentRequest);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Cập nhật học sinh thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        studentService.deleteStudent(id);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Xóa học sinh thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/myInfo")
    public ResponseEntity<?> getStudentInfo() {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        StudentDTO student_dto = studentService.myInfo();
            responseData.setData(student_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
}
