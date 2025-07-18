package com.example.PPQ.Controller;

import com.example.PPQ.Entity.TeacherEntity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.TeacherDTO;
import com.example.PPQ.Service.TeacherService;
import com.example.PPQ.respository.TeacherRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/teachers")
//@PreAuthorize("hasAuthority('ADMIN')")
public class TeacherController {
    @Autowired
    TeacherRespository teacherRespository;
    @Autowired
    TeacherService teacherService;
    @GetMapping
    public ResponseEntity<?> getAllTeachers() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<TeacherDTO> teacher_dto =teacherService.getAllTeacher();
            responseData.setData(teacher_dto);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/myInfo")
    public ResponseEntity<?> myInfo() {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        TeacherDTO teacher_dto = teacherService.myInfo();
            responseData.setData(teacher_dto);
            responseData.setSuccess(Boolean.TRUE);
            responseData.setMessage("Hiện thông tin thành công ");
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
//    @GetMapping(value="/{name}")
//    public ResponseEntity<?> getTeacherById(@PathVariable String name) {
//        ResponseData responseData = new ResponseData();
//        HttpStatus status ;
//        List<Teacher_response> teacher_dto = teacherService.getTeacherByName(name);
//        if(teacher_dto !=null) {
//            responseData.setData(teacher_dto);
//            responseData.setSuccess(Boolean.TRUE);
//            status = HttpStatus.OK;
//
//        }
//        else{
//            responseData.setData(null);
//            responseData.setSuccess(Boolean.FALSE);
//            status = HttpStatus.NOT_FOUND;
//        }
//        return ResponseEntity.status(status).body(responseData);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addTeacher( @Valid  @RequestPart("teacherRequest") String teacherRequestStr,@RequestPart("file") MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        TeacherRequest teacherRequest = null;
        try {
            teacherRequest = objectMapper.readValue(teacherRequestStr, TeacherRequest.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid teacherRequest JSON");
        }


        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        teacherService.addTeacher(teacherRequest, file) ;
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;
            responseData.setMessage("Thêm giáo viên thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @Value("${app.base-url}")
    private String baseUrl;
    @PreAuthorize("hasAuthority('TEACHER') or hasAuthority('ADMIN')")

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable int id, @Valid @RequestPart("teacherRequest") String teacherRequestStr, @RequestPart(value = "file", required = false) MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        TeacherRequest teacherRequest = null;
        try {
            teacherRequest = objectMapper.readValue(teacherRequestStr, TeacherRequest.class);
        } catch (Exception e) {
            System.out.println("Lỗi khi parse teacherRequest: " + e.getMessage());

        }

        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;

      teacherService.updateTeacher(id, teacherRequest, file) ;
            // Lấy thông tin giáo viên sau khi cập nhật
            TeacherEntity updatedTeacher = teacherRespository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại"));
            responseData.setSuccess(true);
            status = HttpStatus.OK;
            responseData.setMessage("Cập nhật giáo viên thành công");
            String Url = baseUrl+"/upload/teachers/";
            responseData.setImagePath(Url+updatedTeacher.getImagePath()); // Đường dẫn đầy đủ
            responseData.setData(null); // Không cần data nếu chỉ dùng imagePath
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        teacherService.deleteTeacher(id);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Xóa giáo viên thành công");

        return ResponseEntity.status(status).body(responseData);
    }
}
