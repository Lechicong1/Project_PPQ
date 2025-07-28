package com.example.PPQ.Payload.Response;

public class StudentDTO {
    private String fullName;
    private int idUsers;
    private String phoneNumber;
    private Integer id;
    private Float score1;
    private Float score2;
    private Float score3;
    private Float totalScore;
    private Float scoreHomework;
    private Integer absentDays;
    private Integer attentedDay;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public Float getScore1() {
        return score1;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
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


    public String getFullName() {
        return fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
