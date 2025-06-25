package com.example.PPQ.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Course")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String nameCourse;
    @Column(columnDefinition = "LONGTEXT")
    private String Description;
    private BigDecimal Fee;
    private Integer numberSessions;
    private String language;

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public Integer getNumberSessions() {
        return numberSessions;
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
}
