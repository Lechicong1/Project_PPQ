package com.example.PPQ.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Student_Class")
public class Student_ClassEntity {
    @Id
    private int ID_Student;
    @Id
    private int ID_Class;
    private float Result;

    public int getID_Student() {
        return ID_Student;
    }

    public void setID_Student(int ID_Student) {
        this.ID_Student = ID_Student;
    }

    public int getID_Class() {
        return ID_Class;
    }

    public void setID_Class(int ID_Class) {
        this.ID_Class = ID_Class;
    }

    public float getResult() {
        return Result;
    }

    public void setResult(float result) {
        Result = result;
    }
}
