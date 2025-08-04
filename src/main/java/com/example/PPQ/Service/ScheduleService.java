package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> gettAllSchedule();

    void addSchedule(ScheduleRequest scheduleRequest);
    void updateSchedule(int id, ScheduleRequest scheduleRequest);
    void deleteSchedule(int id);
    List<ScheduleDTO> getScheduleForStudent(String username);
    List<ScheduleDTO> getScheduleForTeacher(String username);
    List<ScheduleDTO> findScheduleByDayOfWeekAndClass(String dayOfWeek,Integer idClass);
}
