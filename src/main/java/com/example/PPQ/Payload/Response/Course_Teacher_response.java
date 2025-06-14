package com.example.PPQ.Payload.Response;

import jakarta.persistence.Id;

public class Course_Teacher_response {
    private int ID_Course;
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
