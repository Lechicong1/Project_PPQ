package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.AttendanceRequest;
import com.example.PPQ.Payload.Request.StudentCoreRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Service.StudentCoreService;
import com.example.PPQ.ServiceImp.StudentCoreServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/StudentCore")
public class StudentCoreController {
    @Autowired
    StudentCoreService studentCoreService;
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping(value = "/student/{studentId}/class/{classId}")
    public ResponseEntity<?> updateScore(@PathVariable Integer studentId,@PathVariable Integer classId, @RequestBody StudentCoreRequest coreRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        studentCoreService.updateCoreStudent(studentId,classId,coreRequest);
        responseData.setSuccess(Boolean.TRUE);
        responseData.setMessage("Cập nhật điểm thành công");
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @PutMapping()
    public ResponseEntity<?> updateAbsent( @RequestBody List<AttendanceRequest> attendanceRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        studentCoreService.updateAbsentStudent(attendanceRequest);
        responseData.setSuccess(Boolean.TRUE);
        responseData.setMessage("Cập nhật thành công");
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
}
