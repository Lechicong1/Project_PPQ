package com.example.PPQ.Payload.Projection_Interface;

import java.time.LocalTime;

public interface ScheduleView {
        Integer getId();
        Integer getIdClass();
        String getNameRoom();
        String getThu();
        LocalTime getStartTime();
        LocalTime getEndTime();
        String getNameClass();
        String getNameCourse();


}
