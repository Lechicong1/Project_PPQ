package com.example.PPQ.Payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
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


}
