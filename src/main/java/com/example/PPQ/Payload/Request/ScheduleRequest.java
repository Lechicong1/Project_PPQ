package com.example.PPQ.Payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalTime;

public class ScheduleRequest {
    @NotNull(message = "Lớp học không được để trống")
    @Positive(message = "Giá trị phải là số nguyên dương")
    private Integer idClass;
    @NotBlank(message = "Tên phòng học không được để trống")
    private String nameRoom;
    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "giờ học không được để trống")
    private LocalTime startTime;
    @NotNull(message = "giờ học không được để trống")
    private LocalTime endTime;
    @NotBlank(message = "Ngày học không được để trống")
    private String thu;

    public Integer getIdClass() {
        return idClass;
    }

    public void setIdClass(Integer idClass) {
        this.idClass = idClass;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }
}
