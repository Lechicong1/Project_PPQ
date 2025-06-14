package com.example.PPQ.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="Class")
public class ClassesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String className;
    private Integer idCourses;
    private Integer idTeachers;
    private Integer maxStudents;
    private int currentStudents=0;
    private String status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getIdCourses() {
        return idCourses;
    }

    public void setIdCourses(Integer idCourses) {
        this.idCourses = idCourses;
    }

    public Integer getIdTeachers() {
        return idTeachers;
    }

    public void setIdTeachers(Integer idTeachers) {
        this.idTeachers = idTeachers;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(int currentStudents) {
        this.currentStudents = currentStudents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
