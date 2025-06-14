package com.example.PPQ.Payload.Response;

import jakarta.persistence.Id;

public class Schedule_Class_response {
    private int ID_Student;
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
