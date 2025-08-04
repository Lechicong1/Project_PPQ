package com.example.PPQ.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Course_Teacher")
public class Course_TeacherEntity {
    @Id
    private int ID_Course;
    @Id
    private int ID_Teacher;

}
