package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.TeacherEntity;

public class TeacherDTO {
    private String educationLevel;
    private String description;
    private String userName;
    private String fullName;
    private String phoneNumber;
    private int id;
    private String email;
//    private LocalDate startDate;
    private String imagePath;
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
    public TeacherDTO(TeacherEntity e){
        this.educationLevel = e.getEducationLevel();
        this.description = e.getDescription();
        this.id=e.getId();
        this.fullName = e.getFullName();
        this.phoneNumber = e.getPhoneNumber();
        this.email = e.getEmail();

    }
    public TeacherDTO(){}
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
