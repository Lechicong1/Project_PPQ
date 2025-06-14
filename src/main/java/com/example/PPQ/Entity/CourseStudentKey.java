package com.example.PPQ.Entity;

import java.io.Serializable;
import java.util.Objects;

public class CourseStudentKey implements Serializable {
    private int idCourse;
    private int idStudent;
    private int idClass;
    public CourseStudentKey() {}

    public CourseStudentKey(int idCourse, int idStudent, int idClass) {
        this.idCourse = idCourse;
        this.idStudent = idStudent;
        this.idClass = idClass;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseStudentKey)) return false;
        CourseStudentKey that = (CourseStudentKey) o;
        return idCourse == that.idCourse &&
                idStudent == that.idStudent &&
                idClass == that.idClass;
    }


    @Override
    public int hashCode() {
        return Objects.hash(idCourse, idStudent);
    }
}
