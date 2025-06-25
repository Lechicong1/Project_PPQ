package com.example.PPQ.Entity;

import jakarta.persistence.*;

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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
