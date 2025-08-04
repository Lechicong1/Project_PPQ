package com.example.PPQ.Payload.Response;

import com.example.PPQ.Payload.Projection_Interface.ScheduleView;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class ScheduleDTO {
    private Integer id;
    private String nameClass;
    private String nameRoom;
    private LocalTime startTime;
    private LocalTime endTime;
    private String thu;
    private Integer idClass;
    private String nameCourse;
    public ScheduleDTO() {}
    public ScheduleDTO(ScheduleView v){
        this.id = v.getId();
        this.nameClass = v.getNameClass();
        this.nameRoom = v.getNameRoom();
        this.startTime = v.getStartTime();
        this.endTime = v.getEndTime();
        this.thu = v.getThu();
        this.idClass = v.getIdClass();
        this.nameCourse = v.getNameCourse();
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getEndTime() {
        return endTime;
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getStartTime() {
        return startTime;
    }




}
