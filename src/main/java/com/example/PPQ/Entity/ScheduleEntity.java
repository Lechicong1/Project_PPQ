package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.ScheduleRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
@Entity
@Table(name = "Schedule")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idClass;
    private String nameRoom;
    private LocalTime startTime;
    private LocalTime endTime;
    private String thu;
    public ScheduleEntity() {}
    public ScheduleEntity(ScheduleRequest req){
        this.idClass = req.getIdClass();
        this.nameRoom = req.getNameRoom();
        this.startTime = req.getStartTime();
        this.endTime = req.getEndTime();
        this.thu = req.getThu();

    }

}
