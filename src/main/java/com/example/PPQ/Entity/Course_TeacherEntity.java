package com.example.PPQ.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Course_Teacher")
public class Course_TeacherEntity {
    @Id
    private int ID_Course;
    @Id
    private int ID_Teacher;

    public int getID_Course() {
        return ID_Course;
    }

    public void setID_Course(int ID_Course) {
        this.ID_Course = ID_Course;
    }

    public int getID_Teacher() {
        return ID_Teacher;
    }

    public void setID_Teacher(int ID_Teacher) {
        this.ID_Teacher = ID_Teacher;
    }
}
