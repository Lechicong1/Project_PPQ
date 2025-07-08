package com.example.PPQ.Controller;

import com.example.PPQ.Entity.User_Entity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.Schedule_response;
import com.example.PPQ.Service.ScheduleService;
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
    public ResponseEntity<?> getAllSchedules() {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        List<Schedule_response> scheduleResponses =scheduleService.gettAllSchedule();
        if(scheduleResponses.size()>0) {
            responseData.setData(scheduleResponses);
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
    @GetMapping(value="/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        Schedule_response schedule_dto = scheduleService.getScheduleById(id);
        if(schedule_dto !=null) {
            responseData.setData(schedule_dto);
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
    @GetMapping("/myScheduleStudent")
    public ResponseEntity<?> getScheduleForStudent(){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        List<Schedule_response> scheduleResponses =scheduleService.getScheduleForStudent(username);
        if(scheduleResponses !=null) {
            responseData.setData(scheduleResponses);
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
    @GetMapping("/myScheduleTeacher")
    public ResponseEntity<?> getScheduleForTeacher(){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        List<Schedule_response> scheduleResponses =scheduleService.getScheduleForTeacher(username);
        if(scheduleResponses !=null) {
            responseData.setData(scheduleResponses);
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
    public ResponseEntity<?> addSchedule( @Valid @RequestBody ScheduleRequest scheduleRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(scheduleService.addSchedule(scheduleRequest)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;
            responseData.setMessage("schedule added successfully");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("schedule not added successfully");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable int id,@Valid @RequestBody ScheduleRequest scheduleRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(scheduleService.updateSchedule(id,scheduleRequest)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Schedule updated successfully");

        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Schedule not updated successfully");
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status = HttpStatus.OK;
        if(scheduleService.deleteSchedule(id)) {
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
            responseData.setMessage("Schedule deleted successfully");
        }
        else{
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
            responseData.setMessage("Schedule not deleted successfully");

        }
        return ResponseEntity.status(status).body(responseData);
    }
}
