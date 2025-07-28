package com.example.PPQ.Payload.Projection_Interface;

import java.time.LocalDateTime;

public interface CourseRegisterView {
    String getFullName();
    String getNameCourse();
    String getNameClass();
    LocalDateTime getEnrollmentDate();
}
