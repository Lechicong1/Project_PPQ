package com.example.PPQ.Payload.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCoreDTO {
    private Float score1;
    private Float score2;
    private Float score3;
    private Float totalScore;
    private Float scoreHomework;
    private Integer absentDays;
    private Integer attentedDay;
    private String ClassName;
    private String StudentName;
    private String phoneNumber;
    private String result;


}
