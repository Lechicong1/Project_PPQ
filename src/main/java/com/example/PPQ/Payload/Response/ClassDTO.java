package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.ClassesEntity;
import com.example.PPQ.Payload.Projection_Interface.ClassView;

public class ClassDTO {
    private int id;
    private String className;
    private String nameCourse;
    private String nameTeacher;
    private Integer maxStudents;
    private Integer currentStudents;
    private String status;
    private int idCourses;
    private int idTeachers;
    private String roadMap;

    public ClassDTO() {
    }
    public ClassDTO(ClassView classView) {
       this.id = classView.getId();
       this.className=classView.getNameClass();
       this.nameCourse=classView.getNameCourse();
       this.nameTeacher=classView.getNameTeacher();
       this.maxStudents=classView.getMaxStudents();
       this.currentStudents=classView.getCurrentStudents();
       this.status=classView.getStatus();
       this.roadMap=classView.getRoadMap();

    }
    public String getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(String roadMap) {
        this.roadMap = roadMap;
    }

    public int getIdCourses() {
        return idCourses;
    }

    public void setIdCourses(int idCourses) {
        this.idCourses = idCourses;
    }

    public int getIdTeachers() {
        return idTeachers;
    }

    public void setIdTeachers(int idTeachers) {
        this.idTeachers = idTeachers;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getNameTeacher() {
        return nameTeacher;
    }

    public void setNameTeacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(Integer currentStudents) {
        this.currentStudents = currentStudents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
