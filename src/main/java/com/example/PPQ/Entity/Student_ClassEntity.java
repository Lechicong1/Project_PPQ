package com.example.PPQ.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Student_Class")
public class Student_ClassEntity {
    @Id
    private int ID_Student;
    @Id
    private int ID_Class;
    private float Result;

}
