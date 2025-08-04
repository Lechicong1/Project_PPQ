package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Response.CourseDTO;
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
    private String Description;
    private BigDecimal Fee;
    private Integer numberSessions;
    private String language;
    private String imagePath;

}
