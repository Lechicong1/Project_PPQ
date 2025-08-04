package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.ScheduleDTO;
import com.example.PPQ.Service.ScheduleService;
import com.example.PPQ.ServiceImp.ScheduleServiceImp;
import com.example.PPQ.respository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    UsersRepository UsersRepository;
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllSchedules(){
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<ScheduleDTO> scheduleResponses =scheduleService.gettAllSchedule();
            responseData.setData(scheduleResponses);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getSdcheduleByDayOfWeekAndClass(@RequestParam(required = false) String thu,@RequestParam(required = false) Integer idClass){
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<ScheduleDTO> scheduleResponses =scheduleService.findScheduleByDayOfWeekAndClass(thu,idClass);
        responseData.setData(scheduleResponses);
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/myScheduleStudent")
    public ResponseEntity<?> getScheduleForStudent(){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        List<ScheduleDTO> scheduleResponses =scheduleService.getScheduleForStudent(username);
            responseData.setData(scheduleResponses);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/myScheduleTeacher")
    public ResponseEntity<?> getScheduleForTeacher(){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        List<ScheduleDTO> scheduleResponses =scheduleService.getScheduleForTeacher(username);
            responseData.setData(scheduleResponses);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addSchedule( @Valid @RequestBody ScheduleRequest scheduleRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        scheduleService.addSchedule(scheduleRequest);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;
            responseData.setMessage("Thêm lịch học thành công ");
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable int id,@Valid @RequestBody ScheduleRequest scheduleRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        scheduleService.updateSchedule(id,scheduleRequest);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Cập nhật lịch học thành công");
        return ResponseEntity.status(status).body(responseData);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        scheduleService.deleteSchedule(id);
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Xóa lịch học thành công");
        return ResponseEntity.status(status).body(responseData);
    }
}
