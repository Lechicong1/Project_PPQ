package com.example.PPQ.Payload.Response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CourseDTO {
        private Integer id;
        private String nameCourse;
        private String Description;
        private BigDecimal Fee;
        private String imagePath;
        private Integer numberSessions;
        private String language;
        private  String nameClass;

    private Float score1;
    private Float score2;
    private Float score3;
    private Float totalScore;
    private Float scoreHomework;
    private Integer absentDays;
    private Integer attentedDay;
    private String result;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime enrollmentDate;
        public Integer getNumberSessions() {
            return numberSessions;
        }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setNumberSessions(Integer numberSessions) {
            this.numberSessions = numberSessions;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Integer getId() {
        return id;
    }

    public Float getScore1() {
        return score1;
    }

    public void setScore1(Float score1) {
        this.score1 = score1;
    }

    public Float getScore2() {
        return score2;
    }

    public void setScore2(Float score2) {
        this.score2 = score2;
    }

    public Float getScore3() {
        return score3;
    }

    public void setScore3(Float score3) {
        this.score3 = score3;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public Float getScoreHomework() {
        return scoreHomework;
    }

    public void setScoreHomework(Float scoreHomework) {
        this.scoreHomework = scoreHomework;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getAttentedDay() {
        return attentedDay;
    }

    public void setAttentedDay(Integer attentedDay) {
        this.attentedDay = attentedDay;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setId(Integer id) {
        this.id = id;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public BigDecimal getFee() {
        return Fee;
    }

    public void setFee(BigDecimal fee) {
        Fee = fee;
    }
}
