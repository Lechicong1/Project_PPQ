package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.AttendanceRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "Attendance")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idStudent;
    private Integer idClass;
    private LocalDateTime date;
    private String status;
    public AttendanceEntity() {}
    public AttendanceEntity(AttendanceRequest attendanceRequest) {
        this.idStudent = attendanceRequest.getIdStudent();
        this.idClass = attendanceRequest.getIdClass();
        this.status = attendanceRequest.getStatus();
        this.date = LocalDateTime.now();

    }
}
