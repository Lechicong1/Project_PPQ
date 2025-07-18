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
