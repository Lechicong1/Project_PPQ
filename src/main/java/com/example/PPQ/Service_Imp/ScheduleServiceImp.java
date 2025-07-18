package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.ScheduleDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleServiceImp {
    List<ScheduleDTO> gettAllSchedule();
    ScheduleDTO getScheduleById(int id);
    void addSchedule(ScheduleRequest scheduleRequest);
    void updateSchedule(int id, ScheduleRequest scheduleRequest);
    void deleteSchedule(int id);
    List<ScheduleDTO> getScheduleForStudent(String username);
    List<ScheduleDTO> getScheduleForTeacher(String username);
    List<ScheduleDTO> findScheduleByDayOfWeekAndClass(String dayOfWeek,Integer idClass);
}
