package com.example.PPQ.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="StudentCore")
public class StudentCoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float score1;
    private Float score2;
    private Float score3;
    private Float scoreHomework;
    private Integer absentDays;
    private Integer attentedDay;
    private Integer idStudent;
    private Integer idClass;



}
