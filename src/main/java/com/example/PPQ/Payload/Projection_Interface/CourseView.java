package com.example.PPQ.Payload.Projection_Interface;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CourseView {
    String getNameCourse();
    String getNameClass();
    LocalDateTime getEnrollmentDate();
    BigDecimal getFee();
    Integer getNumberSessions();

    Float getScore1();
    Float getScore2();
    Float getScore3();
    Float getScoreHomework();
    Integer getAbsentDays();
    Integer getAttentedDay();

}
