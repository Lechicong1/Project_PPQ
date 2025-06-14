package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.CourseRequest;
import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.Course_response;
import com.example.PPQ.Payload.Response.Schedule_response;

import java.util.List;

public interface ScheduleServiceImp {
    List<Schedule_response> gettAllSchedule();
    Schedule_response getScheduleById(int id);
    boolean addSchedule(ScheduleRequest scheduleRequest);
    boolean updateSchedule(int id, ScheduleRequest scheduleRequest);
    boolean deleteSchedule(int id);
    List<Schedule_response> getScheduleForStudent();
    List<Schedule_response> getScheduleForTeacher();
}
