package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.StudentEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    public StudentDTO() {}
    public StudentDTO(StudentEntity entity){
        this.fullName = entity.getFullName();
        this.id = entity.getId();
        this.phoneNumber = entity.getPhoneNumber();
    }
}
