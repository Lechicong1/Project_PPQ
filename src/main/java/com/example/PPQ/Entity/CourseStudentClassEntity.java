package com.example.PPQ.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "CourseStudentClass")
@IdClass(CourseStudentKey.class)
public class CourseStudentClassEntity {
    @Id
    private Integer idCourse;
    @Id
    private Integer idStudent;
    @Id
    private Integer idClass;
    private LocalDateTime enrollmentDate;


}
