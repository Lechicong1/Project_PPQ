package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.TeacherEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDTO {
    private String educationLevel;
    private String description;
    private String userName;
    private String fullName;
    private String phoneNumber;
    private int id;
    private String email;
    private String imagePath;

    public TeacherDTO(TeacherEntity e){
        this.educationLevel = e.getEducationLevel();
        this.description = e.getDescription();
        this.id=e.getId();
        this.fullName = e.getFullName();
        this.phoneNumber = e.getPhoneNumber();
        this.email = e.getEmail();

    }
    public TeacherDTO(){}

}
