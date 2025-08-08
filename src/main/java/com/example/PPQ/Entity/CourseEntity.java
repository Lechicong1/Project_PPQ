package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.CourseRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table(name = "Course")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nameCourse;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private BigDecimal fee;
    private Integer numberSessions;
    private String language;
    private String imagePath;
    public CourseEntity(CourseRequest courseRequest) {
        this.nameCourse = courseRequest.getNameCourse();
        this.description = courseRequest.getDescription();
        this.fee = courseRequest.getFee();
        this.numberSessions = courseRequest.getNumberSessions();
        this.language = courseRequest.getLanguage();

    }
    public CourseEntity() {}

}
