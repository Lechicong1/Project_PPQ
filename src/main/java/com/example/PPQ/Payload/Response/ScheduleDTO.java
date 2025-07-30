package com.example.PPQ.Payload.Response;

import com.example.PPQ.Payload.Projection_Interface.ScheduleView;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

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

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public Integer getId() {
        return id;
    }
    @JsonFormat(pattern = "HH:mm")
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getIdClass() {
        return idClass;
    }

    public void setIdClass(Integer idClass) {
        this.idClass = idClass;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }
    @JsonFormat(pattern = "HH:mm")
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime starTime) {
        this.startTime = starTime;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }
}
