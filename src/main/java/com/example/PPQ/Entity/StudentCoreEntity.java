package com.example.PPQ.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="StudentCore")
public class StudentCoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float score1;
    private Float score2;
    private Float score3;
    private Float scoreHomework;
    private Integer absentDays;
    private Integer attentedDay;
    private Integer idStudent;
    private Integer idClass;
    public Integer getId() {
        return id;
    }

    public Integer getIdClass() {
        return idClass;
    }

    public void setIdClass(Integer idClass) {
        this.idClass = idClass;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }
}
