package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.ClassRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Class")
public class ClassesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String className;
    private Integer idCourses;
    private Integer idTeachers;
    private Integer maxStudents;
    private int currentStudents=0;
    private String status;
    @Column(columnDefinition = "LONGTEXT")
    private String roadMap;

    public ClassesEntity() {}
    public ClassesEntity(ClassRequest req){
        this.className = req.getClassName();
        this.idCourses = req.getIdCourses();
        this.idTeachers = req.getIdTeachers();
        this.maxStudents = req.getMaxStudents();
        this.status = req.getStatus();
        this.roadMap = req.getRoadMap();

    }

}
